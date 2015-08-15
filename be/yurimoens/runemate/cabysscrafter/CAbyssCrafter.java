package be.yurimoens.runemate.cabysscrafter;

import be.yurimoens.runemate.cabysscrafter.event.CreateRunesEvent;
import be.yurimoens.runemate.cabysscrafter.event.CreateRunesListener;
import be.yurimoens.runemate.cabysscrafter.gui.ConfigurationGui;
import be.yurimoens.runemate.cabysscrafter.task.StopScript;
import be.yurimoens.runemate.cabysscrafter.task.abyss.HandleAbyss;
import be.yurimoens.runemate.cabysscrafter.task.altar.HandleAltar;
import be.yurimoens.runemate.cabysscrafter.task.bank.HandleBank;
import be.yurimoens.runemate.cabysscrafter.task.walking.WalkToAbyss;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CTime;
import be.yurimoens.runemate.util.InvestigateMeteorite;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.Random;
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
        CExecution.delayWhile(() -> !ActionBar.isExpanded() && !ActionBar.toggleExpansion(), Random.nextInt(1500, 2500), 5000, 7000);

        CExecution.delayUntil(this::putGloryOnBar, Random.nextInt(10000, 15000), 58000, 62000);
        CExecution.delayUntil(this::putPouchesOnBar, Random.nextInt(10000, 15000), 58000, 62000);
        CExecution.delayUntil(this::putSkillsOnBar, Random.nextInt(10000, 15000), 58000, 62000);
    }

    private boolean putGloryOnBar() {
        if (ActionBar.getFirstAction("Amulet of glory") == null) {
            SpriteItem amulet;
            if ((amulet = Equipment.getItemIn(Equipment.Slot.NECK)) == null || !amulet.getDefinition().getName().startsWith("Amulet of glory")) {
                ClientUI.sendTrayNotification("No glory found, exiting script.");
                stop();
            }

            boolean equipmentIsOpen = ActionWindow.WORN_EQUIPMENT.isOpen();
            CExecution.delayUntil(ActionWindow.WORN_EQUIPMENT::open, Random.nextInt(4000, 6000), 10000, 12000);

            ActionBar.Slot freeSlot = getFreeSlot();
            if (freeSlot != null) {
                Mouse.drag(amulet, getFreeSlot().getComponent());
            }

            Execution.delay(1200, 1800);

            if (!equipmentIsOpen) {
                CExecution.delayUntil(ActionWindow.WORN_EQUIPMENT::close, Random.nextInt(2000, 3000), 5000, 7000);
            }
        }

        return ActionBar.getFirstAction("Amulet of glory") != null;
    }

    private boolean putPouchesOnBar() {
        boolean inventoryIsOpen = ActionWindow.BACKPACK.isOpen();

        Inventory.getItems(Constants.SMALL_POUCH, Constants.MEDIUM_POUCH, Constants.LARGE_POUCH, Constants.GIANT_POUCH).stream().forEach((pouch) -> {
            if (ActionBar.getFirstAction(pouch.getId()) == null) {
                CExecution.delayUntil(ActionWindow.BACKPACK::open, Random.nextInt(4000, 6000), 10000, 12000);

                ActionBar.Slot freeSlot = getFreeSlot();
                if (freeSlot != null) {
                    Mouse.drag(pouch, getFreeSlot().getComponent());
                }

                Execution.delay(1200, 1800);
            }
        });

        if (!inventoryIsOpen) {
            CExecution.delayUntil(ActionWindow.BACKPACK::close, Random.nextInt(2000, 3000), 5000, 7000);
        }

        for (SpriteItem pouch : Inventory.getItems(Constants.SMALL_POUCH, Constants.MEDIUM_POUCH, Constants.LARGE_POUCH, Constants.GIANT_POUCH)) {
            if (ActionBar.getFirstAction(pouch.getId()) == null) {
                return false;
            }
        }

        return true;
    }

    private boolean putSkillsOnBar() {
        CExecution.delayUntil(() -> putSkillOnBar("Freedom", ActionWindow.DEFENCE_ABILITIES, 1449, 1, 2), Random.nextInt(7000, 10000), 25000, 28000);
        CExecution.delayUntil(() -> putSkillOnBar("Surge", ActionWindow.MAGIC_ABILITIES, 1461, 1, 2), Random.nextInt(7000, 10000), 25000, 28000);

        return (ActionBar.getFirstAction("Freedom") != null && ActionBar.getFirstAction("Surge") != null);
    }

    private boolean putSkillOnBar(String name, ActionWindow actionWindow, int interfaceContainerId, int interfaceComponentId, int interfaceSubComponentId) {
        if (ActionBar.getFirstAction(name) == null) {
            CExecution.delayUntil(actionWindow::open, Random.nextInt(4000, 6000), 10000, 12000);

            InterfaceComponent interfaceSubMenu;

            if (Execution.delayUntil(() -> Interfaces.getAt(interfaceContainerId, 7, 7) != null, 5000, 7000)) {
                interfaceSubMenu = Interfaces.getAt(interfaceContainerId, 7, 7);
            } else {
                return false;
            }

            InterfaceComponent skillComponent;

            if (CExecution.delayUntil(() -> {
                interfaceSubMenu.click();

                return Interfaces.getAt(interfaceContainerId, interfaceComponentId, interfaceSubComponentId) != null;
            }, Random.nextInt(1500, 2500), 5000, 7000)) {
                skillComponent = Interfaces.getAt(interfaceContainerId, interfaceComponentId, interfaceSubComponentId);
            } else {
                return false;
            }

            ActionBar.Slot freeSlot = getFreeSlot();
            if (freeSlot != null) {
                Mouse.drag(skillComponent, getFreeSlot().getComponent());
            }

            Execution.delay(1200, 1800);

            CExecution.delayUntil(actionWindow::close, Random.nextInt(2000, 3000), 5000, 7000);
        }

        return ActionBar.getFirstAction(name) != null;
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
