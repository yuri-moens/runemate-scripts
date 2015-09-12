package be.yurimoens.runemate.clividfarm.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class FixFence extends Task {

    private final int FENCE_POST = 20703;

    private LocatableEntityQueryResults<GameObject> fences;

    @Override
    public boolean validate() {
        return !(fences = GameObjects.newQuery().names("Broken fence").results()).isEmpty() && Inventory.contains(FENCE_POST) && GameObjects.newQuery().names("Diseased livid").results().isEmpty();
    }

    @Override
    public void execute() {
        GameObject fence = fences.nearest();

        if (fence != null && !fence.isVisible()) {
            Camera.turnTo(fence, 0.666D);
        }

        CMouse.fastInteract(fence, "Broken fence");

        Player player = Players.getLocal();
        if (Execution.delayUntil(player::isMoving, 2000, 2500)) {
            Execution.delayWhile(player::isMoving);

            Execution.delay(0, 350);
        }
    }
}
