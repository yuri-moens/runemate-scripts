package be.yurimoens.runemate.cabysscrafter;

import be.yurimoens.runemate.cabysscrafter.event.CreateRunesEvent;
import be.yurimoens.runemate.cabysscrafter.event.CreateRunesListener;
import be.yurimoens.runemate.cabysscrafter.gui.ConfigurationGui;
import be.yurimoens.runemate.cabysscrafter.task.StopScript;
import be.yurimoens.runemate.cabysscrafter.task.abyss.HandleAbyss;
import be.yurimoens.runemate.cabysscrafter.task.altar.HandleAltar;
import be.yurimoens.runemate.cabysscrafter.task.bank.HandleBank;
import be.yurimoens.runemate.cabysscrafter.task.walking.WalkToAbyss;
import be.yurimoens.runemate.util.CTime;
import be.yurimoens.runemate.util.InvestigateMeteorite;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionWindow;
import com.runemate.game.api.rs3.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.TaskScript;
import javafx.application.Platform;

import java.awt.*;

public class CAbyssCrafter extends TaskScript implements PaintListener, CreateRunesListener {

    private StopWatch runtime;
    private int startLevel, startExperience, runesCrafted, lapsDone, essencePrice, runePrice;

    public int bankPreset;
    public int guiReturnCode = -1;
    public int runesToCraft = Integer.MAX_VALUE;
    public long timeToRun = Long.MAX_VALUE;
    public String[] ignoreObstacles;
    public RuneType runeType;
    public InvestigateMeteorite.Skill meteoriteSkill;

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

        InvestigateMeteorite investigateMeteorite = new InvestigateMeteorite(meteoriteSkill);
        StopScript stopScript =  new StopScript(this, runtime, runesToCraft, timeToRun);

        getEventDispatcher().addListener(this);
        getEventDispatcher().addListener(investigateMeteorite);
        setLoopDelay(550, 850);

        setupActionBar();
        runtime.start();

        add(
                stopScript,
                investigateMeteorite,
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

    private void setupActionBar() {
        while (!ActionBar.isExpanded()) {
            ActionBar.toggleExpansion();
            Execution.delay(1500, 2500);
        }

        putGloryOnBar();
        putPouchesOnBar();
        putSkillsOnBar();
    }

    private void putGloryOnBar() {
        if (ActionBar.getFirstAction("Amulet of glory") == null) {
            SpriteItem amulet;
            if ((amulet = Equipment.getItemIn(Equipment.Slot.NECK)) == null || !amulet.getDefinition().getName().startsWith("Amulet of glory")) {
                ClientUI.sendTrayNotification("No glory found, exiting script.");
                stop();
            }

            boolean equipmentIsOpen = ActionWindow.WORN_EQUIPMENT.isOpen();
            while (!ActionWindow.WORN_EQUIPMENT.open()) {
                Execution.delay(4000, 6000);
            }

            while (ActionBar.getFirstAction("Amulet of glory") == null) {
                Mouse.drag(amulet, ActionBar.Slot.ONE.getComponent());
                Execution.delayWhile(() -> ActionBar.getFirstAction("Amulet of glory") == null, 5000, 10000);
            }

            if (!equipmentIsOpen) {
                ActionWindow.WORN_EQUIPMENT.close();
            }
        }
    }

    private void putPouchesOnBar() {
        boolean inventoryIsOpen = ActionWindow.BACKPACK.isOpen();

        Inventory.getItems(Constants.SMALL_POUCH, Constants.MEDIUM_POUCH, Constants.LARGE_POUCH, Constants.GIANT_POUCH).stream().forEach((pouch) -> {
            if (ActionBar.getFirstAction(pouch.getId()) == null) {
                while (!ActionWindow.BACKPACK.open()) {
                    Execution.delay(4000, 6000);
                }

                ActionBar.Slot freeSlot = getFreeSlot();

                while (ActionBar.getFirstAction(pouch.getId()) == null) {
                    Mouse.drag(pouch, freeSlot.getComponent());
                    Execution.delayWhile(() -> ActionBar.getFirstAction(pouch.getId()) == null, 5000, 10000);
                }
            }
        });

        if (!inventoryIsOpen) {
            ActionWindow.BACKPACK.close();
        }
    }

    private void putSkillsOnBar() {
        putSkillOnBar("Freedom", ActionWindow.DEFENCE_ABILITIES, 1449, 1, 2);
        putSkillOnBar("Surge", ActionWindow.MAGIC_ABILITIES, 1461, 1, 2);
    }

    private void putSkillOnBar(String name, ActionWindow actionWindow, int interfaceContainerId, int interfaceComponentId, int interfaceSubComponentId) {
        if (ActionBar.getFirstAction(name) == null) {
            boolean actionWindowIsOpen = actionWindow.isOpen();

            while (!actionWindow.open()) {
                Execution.delay(4000, 6000);
            }

            InterfaceComponent interfaceSubMenu = null;

            while (interfaceSubMenu == null) {
                interfaceSubMenu = Interfaces.getAt(interfaceContainerId, 7, 7);
                Execution.delay(250, 500);
            }

            InterfaceComponent skillComponent = null;

            while (skillComponent == null) {
                interfaceSubMenu.click();
                skillComponent = Interfaces.getAt(interfaceContainerId, interfaceComponentId, interfaceSubComponentId);
                Execution.delay(1500, 2500);
            }

            while (ActionBar.getFirstAction(name) == null) {
                Mouse.drag(skillComponent, getFreeSlot().getComponent());
                Execution.delayWhile(() -> ActionBar.getFirstAction(name) == null, 5000, 10000);
            }

            if (!actionWindowIsOpen) {
                actionWindow.close();
            }
        }
    }

    private ActionBar.Slot getFreeSlot() {
        for (ActionBar.Slot slot : ActionBar.Slot.values()) {
            String name = slot.getAction() == null ? "null" : slot.getAction().getName();
            if (name == null || (!name.startsWith("Amulet") && !name.endsWith("pouch") && !name.equals("Freedom") && !name.equals("Surge"))) {
                return slot;
            }
        }

        return null;
    }
}
