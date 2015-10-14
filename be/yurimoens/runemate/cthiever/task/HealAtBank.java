package be.yurimoens.runemate.cthiever.task;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class HealAtBank extends Task {

    private final Coordinate bank = new Coordinate(3093, 3243, 0);

    @Override
    public boolean validate() {
        return Health.getCurrentPercent() < Random.nextInt(3, 6);
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();
        Coordinate original = player.getPosition();
        RegionPath.buildTo(bank).step();

        if (Execution.delayUntil(player::isMoving, 2000, 3000)) {
            Execution.delayUntil(() -> player.distanceTo(bank) < 3D, 6000, 7000);
        }

        Execution.delayWhile(() -> Health.getCurrent() != Health.getMaximum(), 8000, 10000);

        RegionPath.buildTo(original).step();

        if (Execution.delayUntil(player::isMoving, 2000, 3000)) {
            Execution.delayUntil(() -> player.distanceTo(original) < 3D, 6000, 7000);
        }
    }
}
