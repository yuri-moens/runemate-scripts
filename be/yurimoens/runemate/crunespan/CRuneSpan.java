package be.yurimoens.runemate.crunespan;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.task.TaskScript;

import be.yurimoens.runemate.crunespan.task.SiphonEss;
import be.yurimoens.runemate.crunespan.task.SiphonNode;
import be.yurimoens.runemate.util.CTime;

public class CRuneSpan extends TaskScript implements PaintListener {

    private StopWatch runtime;
    private int startLevel, startExperience, startPoints;

    @Override
    public void onStart(String... args) {
        runtime = new StopWatch();
        runtime.start();
        startLevel = Skill.RUNECRAFTING.getBaseLevel();
        startExperience = Skill.RUNECRAFTING.getExperience();
        startPoints = Integer.parseInt(Interfaces.getAt(1274, 2).getText());

        getEventDispatcher().addListener(this);
        setLoopDelay(350, 550);
        GameEvents.get(GameEvents.RS3.UNEXPECTED_ITEM_HANDLER.getName()).disable();

        add(
                new SiphonNode(),
                new SiphonEss()
        );
    }

    @Override
    public void onPaint(Graphics2D g) {
        int currentLevel = Skill.RUNECRAFTING.getBaseLevel();
        int gainedExperience = Skill.RUNECRAFTING.getExperience() - startExperience;
        int experiencePerHour = (int) (gainedExperience * 3600000D / runtime.getRuntime());
        long timeToNextLevel = experiencePerHour != 0 ? (long) (Skill.RUNECRAFTING.getExperienceToNextLevel() * 3600000D / experiencePerHour) : 0;
        int gainedPoints = Integer.parseInt(Interfaces.getAt(1274, 2).getText()) - startPoints;
        int pointsPerHour = (int) (gainedPoints * 3600000D / runtime.getRuntime());
        final int xOffset = 60;
        final int yOffset = 160;
        final int lineHeight = 20;

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        g.setFont((new Font("Arial", 0, 12)));
        g.drawString("Time running: " + runtime.getRuntimeAsString(), xOffset, yOffset + lineHeight);
        g.drawString("TTL: " + CTime.formatTime(timeToNextLevel), xOffset, yOffset + lineHeight * 2);
        g.drawString("Experience: " + experiencePerHour + " xp/h (" + gainedExperience + ")", xOffset, yOffset + lineHeight * 3);
        g.drawString("Level: " + currentLevel + " (" + (currentLevel - startLevel) + ")", xOffset, yOffset + lineHeight * 4);
        g.drawString("Points: " + pointsPerHour + " points/h (" + gainedPoints + ")", xOffset, yOffset + lineHeight * 5);
    }
}
