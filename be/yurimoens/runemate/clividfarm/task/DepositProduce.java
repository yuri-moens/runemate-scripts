package be.yurimoens.runemate.clividfarm.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class DepositProduce extends Task {

    private final int BUNCH = 20705;

    @Override
    public boolean validate() {
        return Inventory.contains(BUNCH) && GameObjects.newQuery().names("Diseased livid").results().isEmpty();
    }

    @Override
    public void execute() {
        GameObject wagon = GameObjects.newQuery().names("Trade wagon").results().first();

        if (wagon == null) {
            return;
        }

        if (!wagon.isVisible()) {
            Camera.turnTo(wagon, 0.666D);
        }

        CMouse.fastInteract(wagon, "Deposit");

        Player player = Players.getLocal();
        if (Execution.delayUntil(player::isMoving, 1200, 1800)) {
            Execution.delayWhile(player::isMoving);

            Execution.delayWhile(() -> Inventory.contains(BUNCH), 1500, 2000);
        }
    }
}
