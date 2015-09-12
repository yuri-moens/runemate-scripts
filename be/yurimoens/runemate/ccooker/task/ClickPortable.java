package be.yurimoens.runemate.ccooker.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.task.Task;

public class ClickPortable extends Task {

    private final int PORTABLE_MODEL = -2058420623;
    private final Timer timeout = new Timer(Random.nextInt(3000, 5000));

    @Override
    public boolean validate() {
        return ((!timeout.isRunning())
                && Interfaces.getAt(1371, 0) != null
                && !GameObjects.newQuery().models(PORTABLE_MODEL).results().isEmpty());
    }

    @Override
    public void execute() {
        timeout.reset();
        if (!timeout.isRunning()) {
            timeout.start();
        }

//        if (Players.getLocal().isMoving()) {
//            CMouse.hopClick(GameObjects.newQuery().models(PORTABLE_MODEL).results().nearest());
//        } else {
//            CMouse.hopInteract(GameObjects.newQuery().models(PORTABLE_MODEL).results().nearest(), "Cook");
//        }
        GameObjects.newQuery().models(PORTABLE_MODEL).results().nearest().interact("Cook");
    }
}
