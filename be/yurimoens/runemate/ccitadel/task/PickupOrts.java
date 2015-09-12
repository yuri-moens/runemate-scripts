package be.yurimoens.runemate.ccitadel.task;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class PickupOrts extends Task {

    private final int ORT_ID = 24909;
    private final int[] UNINTERRUPTIBLE_ANIMATIONS = { 5562 };

    private GroundItem ort;

    @Override
    public boolean validate() {
        ort = GroundItems.newQuery().filter((groundItem) -> groundItem.getId() == 24909).results().nearest();
        return (ort != null && (!Inventory.isFull() || Inventory.contains(ORT_ID)));
    }

    @Override
    public void execute() {
        int ortQuantity = Inventory.getQuantity(ORT_ID);

        if (!ort.isVisible()) {
            Camera.turnTo(ort);
        }

        Execution.delayWhile(() -> {
            for (int animationId : UNINTERRUPTIBLE_ANIMATIONS) {
                if (Players.getLocal().getAnimationId() == animationId) {
                    return true;
                }
            }

            return false;
        }, 4000, 5000);

        ort.click();

        Execution.delayUntil(() -> ortQuantity < Inventory.getQuantity(ORT_ID), 1200, 2100);
    }
}
