package be.yurimoens.runemate.ccooker.task;

import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.framework.task.Task;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CheckIdle extends Task {

    private final TaskScript script;
    private final Timer timeout;
    private int lastExperience = 0;

    public CheckIdle(TaskScript script) {
        this.script = script;
        lastExperience = Skill.COOKING.getExperience();
        timeout = new Timer(240000L);
        timeout.start();
    }

    @Override
    public boolean validate() {
        return timeout.getRemainingTime() == 0;
    }

    @Override
    public void execute() {
        if (lastExperience != Skill.COOKING.getExperience()) {
            lastExperience = Skill.COOKING.getExperience();
            timeout.reset();
        } else {
            RuneScape.logout();
            script.stop();
        }
    }
}
