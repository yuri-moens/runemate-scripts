package be.yurimoens.runemate.cslayeraid.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.task.Task;

public class Eat extends Task {

    private Timer timeout;

    @Override
    public boolean validate() {
        return (Health.getCurrentPercent() < Random.nextInt(45, 55)
                && (timeout == null || !timeout.isRunning()));
    }

    @Override
    public void execute() {
        timeout = new Timer(Random.nextInt(3000, 5000));
        timeout.start();

        Keyboard.type("5", false);
    }
}
