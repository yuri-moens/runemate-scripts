package be.yurimoens.runemate.crunespan.task;

import be.yurimoens.runemate.crunespan.Nodes;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class SiphonNode extends Task {

    private GameObject node;

    @Override
    public boolean validate() {
        return (Players.getLocal().getAnimationId() == -1
                && !Players.getLocal().isMoving()
                && Inventory.getQuantity(24227) > Random.nextInt(100, 150)
                && (node = Nodes.getHighestNode(node)) != null);
    }

    @Override
    public void execute() {
        if (node != null) {
            if (!node.isVisible()) {
                Camera.turnTo(node);
                Execution.delay(400, 800);
            }
            
            CExecution.delayUntil(() -> CMouse.fastInteract(node, "Siphon"), Random.nextInt(450, 650), 3600, 4000);
            
            if (Random.nextInt(5) < 4) {
                CMouse.moveRandomFromPosition();
            }

            if (Execution.delayUntil(() -> Players.getLocal().getAnimationId() != -1 || Players.getLocal().isMoving(), 2200, 2900)) {
                Execution.delayWhile(() -> Players.getLocal().isMoving());
                Execution.delay(2000, 3000);
                if (Players.getLocal().getAnimationId() == -1) {
                    node = null;
                }
            }
        }
    }
}
