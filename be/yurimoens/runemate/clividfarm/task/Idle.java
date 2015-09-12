package be.yurimoens.runemate.clividfarm.task;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Idle extends Task {

    private final Coordinate startPoint = new Coordinate(2107, 3946, 0);
    private final int LUMBER = 20702;

    private final TakeLumber takeLumber;
    private final ConvertLumber convertLumber;

    public Idle(TakeLumber takeLumber, ConvertLumber convertLumber) {
        this.takeLumber = takeLumber;
        this.convertLumber = convertLumber;
    }

    @Override
    public boolean validate() {
        return (Players.getLocal().distanceTo(startPoint) > 2D || Inventory.contains(LUMBER) || Inventory.getUsedSlots() <= 13)
                && GameObjects.newQuery().names("Diseased livid").results().isEmpty();
    }

    @Override
    public void execute() {
        if (Inventory.getUsedSlots() <= 13) {
            takeLumber.execute();
        } else if (Players.getLocal().distanceTo(startPoint) > 2D) {
            Player player = Players.getLocal();

            if (startPoint.isVisible()) {
                startPoint.click();
            } else {
                PredefinedPath path = PredefinedPath.create(new Coordinate(startPoint.getX(), startPoint.getY() - 1, startPoint.getPlane()));

                if (path != null) {
                    path.step(true);
                }
            }

            if (Execution.delayUntil(player::isMoving, 1200, 1800)) {
                Execution.delayWhile(player::isMoving);
            }
        } else {
            convertLumber.execute();
        }
    }
}
