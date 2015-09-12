package be.yurimoens.runemate.cfisher.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.web.WebPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Lodestone;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class WalkToBank extends Task {

    private final int[] URNS = { 20334, 20335, 20336 };
    private final Area.Circular bankArea = new Area.Circular(new Coordinate(3093, 3490), 5D);
    private final Coordinate edgevilleLodestone = new Coordinate(3067, 3504, 0);

    public WalkToBank() {
        add(new Drop());
    }

    @Override
    public boolean validate() {
        return (!Inventory.containsAnyOf(URNS) && !bankArea.contains(Players.getLocal()));
    }

    @Override
    public void execute() {
        getChildren().stream().forEach((task) -> task.execute());
        Lodestone.EDGEVILLE.teleport();
            Execution.delayWhile(() -> (Players.getLocal().isMoving() || Players.getLocal().getAnimationId() != -1)
                    && Players.getLocal().distanceTo(edgevilleLodestone) > 3);

            WebPath path = Traversal.getDefaultWeb().getPathBuilder().buildTo(bankArea);
            while (Players.getLocal().distanceTo(path.getDestination()) > 3) {
                path.step(true);
            }
    }
}
