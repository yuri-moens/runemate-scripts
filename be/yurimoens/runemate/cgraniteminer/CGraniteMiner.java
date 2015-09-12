package be.yurimoens.runemate.cgraniteminer;

import be.yurimoens.runemate.cgraniteminer.task.EquipSign;
import be.yurimoens.runemate.cgraniteminer.task.MineGranite;
import be.yurimoens.runemate.util.InvestigateMeteorite;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public final class CGraniteMiner extends TaskScript implements PaintListener {

    private String aSetting;
    private StopWatch runtime;
    private long startTime;
    private int startLevel, startExperience;

    @Override
    public void onStart(String... args) {
//        AbstractScript script = this;
//        final ConfigurationGui gui;
//
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                gui = new ConfigurationGui("CGraniteMiner configuration", script);
//                gui.show();
//            }
//        });
//
//        while (gui.returnCode == 0) {
//            Execution.delay(500);
//        }
//
//        if (gui.returnCode == 2) {
//            stop();
//            return;
//        }

        runtime = new StopWatch();
        runtime.start();
        startTime = System.currentTimeMillis();
        startLevel = Skill.MINING.getCurrentLevel();
        startExperience = Skill.MINING.getExperience();

        getEventDispatcher().addListener(this);
        setLoopDelay(230, 670);
        aSetting = getSettings().getProperty("setting");

        add(new EquipSign(), new MineGranite(), new InvestigateMeteorite(InvestigateMeteorite.Skill.DIVINATION));
    }

    @Override
    public void onPaint(Graphics2D g) {
        int currentLevel = Skill.MINING.getCurrentLevel();
        int gainedExperience = Skill.MINING.getExperience() - startExperience;
        int experiencePerHour = (int) (gainedExperience * 3600000D / (System.currentTimeMillis() - startTime));
        long timeToNextLevel = experiencePerHour != 0 ? (long) (Skill.MINING.getExperienceToNextLevel() * 3600000D / experiencePerHour) : 0;

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        g.setFont((new Font("Arial", 0, 12)));
        g.drawString("Time running: " + runtime.getRuntimeAsString(), 10, 20);
        g.drawString("TTL: " + formatTime(timeToNextLevel), 10, 40);
        g.drawString("Experience: " + experiencePerHour + " xp/h (" + gainedExperience + ")", 10, 60);
        g.drawString("Level: " + currentLevel + " (" + (currentLevel - startLevel) + ")", 10, 80);
    }

    public String formatTime(long time) {
        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                        .toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(time)));

        return hms;
    }

}