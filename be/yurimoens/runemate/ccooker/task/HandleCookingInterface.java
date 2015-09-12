package be.yurimoens.runemate.ccooker.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.task.Task;

public class HandleCookingInterface extends Task {

    private final Timer timeout = new Timer(Random.nextInt(2000, 4000));

    @Override
    public boolean validate() {
        return ((!timeout.isRunning()) && Interfaces.getAt(1371, 0) != null);
    }

    @Override
    public void execute() {
        timeout.reset();
        if (!timeout.isRunning()) {
            timeout.start();
        }

        Keyboard.type(" ", false);
    }
}
