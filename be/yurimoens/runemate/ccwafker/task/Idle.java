package be.yurimoens.runemate.ccwafker.task;

import be.yurimoens.runemate.ccwafker.Constants;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.task.Task;

public class Idle extends Task {

    private Timer timeout;

    @Override
    public boolean validate() {
        for (Area.Circular gameArea : Constants.gameAreas) {
            if (gameArea.contains(Players.getLocal())) {
                return (timeout == null || !timeout.isRunning());
            }
        }

        return false;
    }

    @Override
    public void execute() {
        timeout = new Timer(Random.nextInt(120000));
        timeout.start();

        if (Random.nextInt(5) == 1) {
            RegionPath path = RegionPath.buildTo(Players.getLocal().getPosition().randomize(2, 2));
            if (path != null) {
                path.step(true);
            } else {
                Camera.turnTo(Random.nextInt(360));
            }
        } else {
            Camera.turnTo(Random.nextInt(360));
        }
    }
}
