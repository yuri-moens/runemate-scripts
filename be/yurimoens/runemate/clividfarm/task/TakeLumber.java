package be.yurimoens.runemate.clividfarm.task;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class TakeLumber extends Task {

    private final int LUMBER = 20702;
    private final int FENCE_POST = 20703;

    @Override
    public boolean validate() {
        return Inventory.getItems(LUMBER).size() < 2 && Inventory.getItems(FENCE_POST).size() < 2 && GameObjects.newQuery().names("Diseased livid").results().isEmpty();
    }

    @Override
    public void execute() {
        GameObject logPile = GameObjects.newQuery().names("Log pile").results().first();

        if (!logPile.isVisible()) {
            Camera.turnTo(logPile, 0.666D);
        }

        logPile.interact("Take-5");

        Execution.delayUntil(() -> Inventory.contains(LUMBER), 3000, 3800);
    }
}
