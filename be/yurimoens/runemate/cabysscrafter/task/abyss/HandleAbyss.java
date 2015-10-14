package be.yurimoens.runemate.cabysscrafter.task.abyss;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.Location;
import be.yurimoens.runemate.cabysscrafter.RuneType;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.AbstractScript;
import com.runemate.game.api.script.framework.task.Task;

public class HandleAbyss extends Task {

    public HandleAbyss(AbstractScript script, RuneType runeType, String[] ignoreObstacles) {
        HandleObstacle handleObstacle = new HandleObstacle(runeType.RIFT_ID, ignoreObstacles);
        script.getEventDispatcher().addListener(handleObstacle);

        add(handleObstacle, new FixPouches(), new EnterRift(runeType.RIFT_ID));
    }

    @Override
    public boolean validate() {
        Location location = Location.getLocation();
        return location == Location.ABYSS || location == Location.INNER_RING;
    }

    @Override
    public void execute() {}
}
