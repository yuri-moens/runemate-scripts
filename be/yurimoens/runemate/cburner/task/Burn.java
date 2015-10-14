package be.yurimoens.runemate.cburner.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Burn extends Task {

    private GameObject brazier;

    @Override
    public boolean validate() {
        return Inventory.contains(1517) && (brazier = GameObjects.newQuery().names("Portable brazier").results().nearest()) != null;
    }

    @Override
    public void execute() {
        boolean isBurning = false;

        if (!Inventory.isFull()) {
            if (Execution.delayUntil(() -> Players.getLocal().getAnimationId() != -1, 3000, 4000)) {
                isBurning = true;
            }
        }

        if (!isBurning) {
            CMouse.fastInteract(brazier, "Add logs");
        }
    }
}
