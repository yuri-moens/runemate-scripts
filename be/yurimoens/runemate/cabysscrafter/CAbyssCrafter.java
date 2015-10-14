package be.yurimoens.runemate.cabysscrafter;

import be.yurimoens.runemate.cabysscrafter.event.CreateRunesEvent;
import be.yurimoens.runemate.cabysscrafter.event.CreateRunesListener;
import be.yurimoens.runemate.cabysscrafter.gui.ConfigurationGui;
import be.yurimoens.runemate.cabysscrafter.task.StopScript;
import be.yurimoens.runemate.cabysscrafter.task.abyss.HandleAbyss;
import be.yurimoens.runemate.cabysscrafter.task.altar.HandleAltar;
import be.yurimoens.runemate.cabysscrafter.task.bank.HandleBank;
import be.yurimoens.runemate.cabysscrafter.task.summoning.RenewPoints;
import be.yurimoens.runemate.cabysscrafter.task.summoning.SummonFamiliar;
import be.yurimoens.runemate.cabysscrafter.task.walking.WalkToAbyss;
import be.yurimoens.runemate.util.CTime;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.TaskScript;
import javafx.application.Platform;

import java.awt.*;
import java.util.function.Predicate;

public class CAbyssCrafter extends TaskScript implements PaintListener, CreateRunesListener {

    /**
     * Variables used for outputting run information by the painter.
     */
    private StopWatch runtime;
    private int startLevel, startExperience, runesCrafted, lapsDone, essencePrice, runePrice;

    /**
     * The bank preset to use. Can be either 1 or 2.
     */
    public int bankPreset;

    /**
     * Return code of the GUI. -1 = still running, 0 = exited normally, anything else = error
     */
    public int guiReturnCode = -1;

    /**
     * The amount of runes to craft before stopping the script.
     */
    public int runesToCraft = Integer.MAX_VALUE;

    /**
     * The time in ms to run the script.
     */
    public long timeToRun = Long.MAX_VALUE;

    /**
     * Array with the names of Abyss obstacles to ignore.
     */
    public String[] ignoreObstacles;

    /**
     * The rune type to craft.
     */
    public RuneType runeType;

    @Override
    public void onStart(String... args) {
        Platform.runLater(() -> new ConfigurationGui(this));

        Execution.delayWhile(() -> guiReturnCode == -1);

        switch (guiReturnCode) {
            case 1: System.out.println("Something bad happened!"); // fall through
            case 2:
                System.out.println("Configuration exited.");
                stop();
                return;
        }

        runtime = new StopWatch();
        startLevel = Skill.RUNECRAFTING.getBaseLevel();
        startExperience = Skill.RUNECRAFTING.getExperience();
        essencePrice = GrandExchange.lookup(Constants.PURE_ESSENCE).getPrice();
        runePrice = GrandExchange.lookup(runeType.RUNE_ID).getPrice();
        GameEvents.get(GameEvents.RS3.UNEXPECTED_ITEM_HANDLER.getName()).disable();

        StopScript stopScript;

        getEventDispatcher().addListener(this);
        setLoopDelay(600, 950);

        runtime.start();

        add(
                (stopScript = new StopScript(this, runtime, runesToCraft, timeToRun)),
                new RenewPoints(),
                new SummonFamiliar(),
                new HandleBank(bankPreset),
                new WalkToAbyss(),
                new HandleAbyss(this, runeType, ignoreObstacles),
                new HandleAltar(runeType, this, stopScript)
        );
    }

    @Override
    public void onPaint(Graphics2D g) {
        int currentLevel = Skill.RUNECRAFTING.getBaseLevel();
        int gainedExperience = Skill.RUNECRAFTING.getExperience() - startExperience;
        int experiencePerHour = (int) (gainedExperience * 3600000D / runtime.getRuntime());
        long timeToNextLevel = experiencePerHour != 0 ? (long) (Skill.RUNECRAFTING.getExperienceToNextLevel() * 3600000D / experiencePerHour) : 0;
        int runesCraftedPerHour = (int) (runesCrafted * 3600000D / runtime.getRuntime());
        long averageLapTime = (lapsDone == 0) ? 0 : runtime.getRuntime() / lapsDone;
        final int xOffset = 60;
        final int yOffset = 10;
        final int lineHeight = 20;

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        g.setFont((new Font("Arial", 0, 12)));
        g.drawString("Time running: " + runtime.getRuntimeAsString(), xOffset, yOffset + lineHeight);
        g.drawString("TTL: " + CTime.formatTime(timeToNextLevel), xOffset, yOffset + lineHeight * 2);
        g.drawString("Experience: " + experiencePerHour + " xp/h (" + gainedExperience + ")", xOffset, yOffset + lineHeight * 3);
        g.drawString("Level: " + currentLevel + " (" + (currentLevel - startLevel) + ")", xOffset, yOffset + lineHeight * 4);
        g.drawString("Runes crafted: " + runesCraftedPerHour + " runes/h (" + runesCrafted + ")", xOffset, yOffset + lineHeight * 5);
        g.drawString("Profit: " + Math.round(runesCraftedPerHour * (runePrice - essencePrice) / 1000) + "k gp/h ("
                + Math.round(runesCrafted * (runePrice - essencePrice) / 1000) + "k gp)", xOffset, yOffset + lineHeight * 6);
        g.drawString("Average lap time: " + CTime.formatTime(averageLapTime), xOffset, yOffset + lineHeight * 7);
    }

    @Override
    public void createRunesEventReceived(CreateRunesEvent event) {
        runesCrafted += event.RUNES_CRAFTED;
        lapsDone++;
    }

    @Override
    public void onPause() {
    	if (runtime != null && runtime.isRunning()) {
    		runtime.stop();
    	}
    }
    
    @Override
    public void onResume() {
    	if (runtime != null && !runtime.isRunning()) {
    		runtime.start();
    	}
    }
}
