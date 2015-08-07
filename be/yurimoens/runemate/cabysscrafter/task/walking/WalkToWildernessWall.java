package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class WalkToWildernessWall extends Task {

    @Override
    public boolean validate() {
        return (getParent().validate()
                && Players.getLocal().getPosition().getY() < Constants.wildernessWall.getY()
                && Players.getLocal().distanceTo(Constants.wildernessWall) > 8D);
    }

    @Override
    public void execute() {
        Coordinate nearWall = new Coordinate(Constants.wildernessWall.getX(), Constants.wildernessWall.getY() - 2);

        RegionPath.buildTo(nearWall).step(true);

        if (Execution.delayUntil(() -> Players.getLocal().isMoving(), 1200, 1800)) {
            Execution.delayUntil(() -> Players.getLocal().distanceTo(Constants.wildernessWall) < Random.nextDouble(6.5D, 8D), 13000, 16000);
        }

    }
}
