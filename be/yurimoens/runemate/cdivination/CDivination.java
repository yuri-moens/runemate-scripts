package be.yurimoens.runemate.cdivination;

import be.yurimoens.runemate.cdivination.task.CaptureChronicleFragment;
import be.yurimoens.runemate.cdivination.task.ConvertMemories;
import be.yurimoens.runemate.cdivination.task.HandInChronicles;
import be.yurimoens.runemate.cdivination.task.RunFromCombat;
import be.yurimoens.runemate.cdivination.task.harvest.Harvest;
import be.yurimoens.runemate.util.CTime;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;

public class CDivination extends TaskScript implements PaintListener, ChatboxListener {

    private StopWatch runtime;
    private int startLevel, startExperience;

    public static boolean handInFragments = false;

    public int guiReturnCode = -1;
    public int energyToHarvest = Integer.MAX_VALUE;
    public int chronicleFragmentAmount = 10;
    public long timeToRun = Long.MAX_VALUE;
    public String action;

    @Override
    public void onStart(String... args) {
        action = "enhanced experience";

        runtime = new StopWatch();
        runtime.start();
        startLevel = Skill.DIVINATION.getBaseLevel();
        startExperience = Skill.DIVINATION.getExperience();

        getEventDispatcher().addListener(this);
        setLoopDelay(350, 550);

        add(
                new RunFromCombat(),
                new HandInChronicles(getLocation()),
                new CaptureChronicleFragment(),
                new Harvest(),
                new ConvertMemories(action)
        );
    }

    @Override
    public void onPaint(Graphics2D g) {
        int currentLevel = Skill.DIVINATION.getBaseLevel();
        int gainedExperience = Skill.DIVINATION.getExperience() - startExperience;
        int experiencePerHour = (int) (gainedExperience * 3600000D / runtime.getRuntime());
        long timeToNextLevel = experiencePerHour != 0 ? (long) (Skill.DIVINATION.getExperienceToNextLevel() * 3600000D / experiencePerHour) : 0;
        final int xOffset = 60;
        final int yOffset = 10;
        final int lineHeight = 20;

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        g.setFont((new Font("Arial", 0, 12)));
        g.drawString("Time running: " + runtime.getRuntimeAsString(), xOffset, yOffset + lineHeight);
        g.drawString("TTL: " + CTime.formatTime(timeToNextLevel), xOffset, yOffset + lineHeight * 2);
        g.drawString("Experience: " + experiencePerHour + " xp/h (" + gainedExperience + ")", xOffset, yOffset + lineHeight * 3);
        g.drawString("Level: " + currentLevel + " (" + (currentLevel - startLevel) + ")", xOffset, yOffset + lineHeight * 4);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("You have " + chronicleFragmentAmount + " chronicle fragments.")) {
            handInFragments = true;
        }
    }

    private Coordinate getLocation() {
        Npc wisp = Npcs.newQuery().filter((npc) -> npc.getDefinition().getName().endsWith("wisp")).results().first();

        if (wisp == null) {
            return null;
        }

        Coordinate location = null;

        switch (wisp.getName()) {
            case "Lustrous wisp": location = new Coordinate(3469, 3536, 0); break;
        }

        return location;
    }
}
