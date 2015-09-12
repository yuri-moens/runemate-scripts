package be.yurimoens.runemate.ccooker;

import be.yurimoens.runemate.ccooker.event.HandleBankEvent;
import be.yurimoens.runemate.ccooker.event.HandleBankListener;
import be.yurimoens.runemate.ccooker.task.CheckIdle;
import be.yurimoens.runemate.ccooker.task.Cook;
import be.yurimoens.runemate.ccooker.task.HandleBank;
import be.yurimoens.runemate.util.CTime;
import be.yurimoens.runemate.util.InvestigateMeteorite;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;

public class CCooker extends TaskScript implements PaintListener, HandleBankListener {

    private StopWatch runtime;
    private int startLevel, startExperience, foodCooked;

    @Override
    public void onStart(String... args) {
        runtime = new StopWatch();
        runtime.start();
        startLevel = Skill.COOKING.getBaseLevel();
        startExperience = Skill.COOKING.getExperience();

        InvestigateMeteorite investigateMeteorite = new InvestigateMeteorite(InvestigateMeteorite.Skill.DIVINATION);

        getEventDispatcher().addListener(this);
        getEventDispatcher().addListener(investigateMeteorite);
        setLoopDelay(220, 470);
        add(investigateMeteorite, new Cook(), new HandleBank(this), new CheckIdle(this)); //
    }

    @Override
    public void onPaint(Graphics2D g) {
        int currentLevel = Skill.COOKING.getBaseLevel();
        int gainedExperience = Skill.COOKING.getExperience() - startExperience;
        int experiencePerHour = (int) (gainedExperience * 3600000D / runtime.getRuntime());
        long timeToNextLevel = experiencePerHour != 0 ? (long) (Skill.COOKING.getExperienceToNextLevel() * 3600000D / experiencePerHour) : 0;
        int foodCookedPerHour = (int) (foodCooked * 3600000D / runtime.getRuntime());

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        g.setFont((new Font("Arial", 0, 12)));
        g.drawString("Time running: " + runtime.getRuntimeAsString(), 10, 20);
        g.drawString("TTL: " + CTime.formatTime(timeToNextLevel), 10, 40);
        g.drawString("Experience: " + experiencePerHour + " xp/h (" + gainedExperience + ")", 10, 60);
        g.drawString("Level: " + currentLevel + " (" + (currentLevel - startLevel) + ")", 10, 80);
        g.drawString("Food cooked: " + foodCookedPerHour + " food/h (" + foodCooked + ")", 10, 100);
    }

    @Override
    public void handleBankEventReceived(HandleBankEvent event) {
        foodCooked += event.foodCooked;
    }
}
