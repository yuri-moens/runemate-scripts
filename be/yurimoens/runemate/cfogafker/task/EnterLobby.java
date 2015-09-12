package be.yurimoens.runemate.cfogafker.task;

import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

/**
 * Created by yurim on 2015-08-13.
 */
public class EnterLobby extends Task {

    private GameObject passageWay;

    @Override
    public boolean validate() {
        return Players.getLocal().getPosition().getY() < 5650;
    }

    @Override
    public void execute() {
        Coordinate coord = new Coordinate(1716, 5599, 0).randomize(0, 2);
        if (Players.getLocal().distanceTo(coord) > 3D) {
            RegionPath.buildTo(coord).step(true);
        }

        Execution.delay(1200, 1800);
        Execution.delayWhile(() -> Players.getLocal().isMoving());

        CExecution.delayUntil(() -> CMouse.fastInteract(new Coordinate(1719, Random.nextInt(5598, 6000)), "Go-through"), Random.nextInt(500, 750), 5000, 7000);
        Execution.delay(2000, 3000);
    }
}
