package be.yurimoens.runemate.cfletcher;

import be.yurimoens.runemate.cfletcher.event.HandleBankEvent;
import be.yurimoens.runemate.cfletcher.event.HandleBankListener;
import be.yurimoens.runemate.cfletcher.task.CutLogs;
import be.yurimoens.runemate.cfletcher.task.HandleBank;
import be.yurimoens.runemate.cfletcher.task.StringBows;
import be.yurimoens.runemate.util.CTime;
import be.yurimoens.runemate.util.InvestigateMeteorite;
import com.runemate.game.api.client.paint.PaintListener;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.task.TaskScript;

import java.awt.*;

public class CFletcher extends TaskScript implements PaintListener, HandleBankListener {

    public enum Action {
        CUTTING("Cutting"),
        STRINGING("Stringing"),
        CUTTING_STRINGING("Cutting & stringing");

        private final String NAME;

        Action(String name) {
            this.NAME = name;
        }

        @Override
        public String toString() {
            return this.NAME;
        }
    }

    private StopWatch runtime;
    private int startLevel, startExperience, bowsStrung, logsCut;
    private Action action;

    @Override
    public void onStart(String... args) {
        runtime = new StopWatch();
        runtime.start();
        startLevel = Skill.FLETCHING.getCurrentLevel();
        startExperience = Skill.FLETCHING.getExperience();
        action = Action.STRINGING;

        getEventDispatcher().addListener(this);
        setLoopDelay(230, 670);

        HandleBank handleBank = new HandleBank();
        handleBank.addHandleBankListener(this);
        add(handleBank, new InvestigateMeteorite(InvestigateMeteorite.Skill.FLETCHING));

        switch (action) {
            case CUTTING: add(new CutLogs()); break;
            case STRINGING: add(new StringBows()); break;
            case CUTTING_STRINGING: add(new CutLogs(), new StringBows()); break;
        }
    }

    @Override
    public void onPaint(Graphics2D g) {
        int currentLevel = Skill.FLETCHING.getCurrentLevel();
        int gainedExperience = Skill.FLETCHING.getExperience() - startExperience;
        int experiencePerHour = (int) (gainedExperience * 3600000D / runtime.getRuntime());
        long timeToNextLevel = experiencePerHour != 0 ? (long) (Skill.FLETCHING.getExperienceToNextLevel() * 3600000D / experiencePerHour) : 0;
        int logsCutPerHour = (int) (logsCut * 3600000D / runtime.getRuntime());
        int bowsStrungPerHour = (int) (bowsStrung * 3600000D / runtime.getRuntime());

        g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF));
        g.setFont((new Font("Arial", 0, 12)));
        g.drawString("Time running: " + runtime.getRuntimeAsString(), 10, 20);
        g.drawString("TTL: " + CTime.formatTime(timeToNextLevel), 10, 40);
        g.drawString("Experience: " + experiencePerHour + " xp/h (" + gainedExperience + ")", 10, 60);
        g.drawString("Level: " + currentLevel + " (" + (currentLevel - startLevel) + ")", 10, 80);
        switch (action) {
            case CUTTING: g.drawString("Logs cut: " + logsCutPerHour + " logs/h (" + logsCut + ")", 10, 100); break;
            case STRINGING: g.drawString("Bows strung: " + bowsStrungPerHour + " bows/h (" + bowsStrung + ")", 10, 100); break;
            case CUTTING_STRINGING:
                g.drawString("Logs cut: " + logsCutPerHour + " logs/h (" + logsCut + ")", 10, 100);
                g.drawString("Bows strung: " + bowsStrungPerHour + " bows/h (" + bowsStrung + ")", 10, 120); break;
        }
    }

    @Override
    public void handleBankEventReceived(HandleBankEvent event) {
        this.bowsStrung += event.bowsStrung;
        this.logsCut += event.logsCut;
    }
}
