package be.yurimoens.runemate.crunespan.task;

import be.yurimoens.runemate.crunespan.EssNpcs;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class SiphonEss extends Task {

    private Npc essNpc;

    @Override
    public boolean validate() {
        return (Players.getLocal().getAnimationId() == -1
                && !Players.getLocal().isMoving()
                && (essNpc = EssNpcs.getHighestEssNpc(essNpc)) != null);
    }

    @Override
    public void execute() {
        String action = "Siphon";
        if (Inventory.getQuantity(24227) < 25) {
            essNpc = Npcs.newQuery().models(-1245572230).results().nearest();
            action = "Collect";
            if (essNpc == null) {
                return;
            }
        }
        
        final String action2 = action;

        if (!essNpc.isVisible()) {
            Camera.turnTo(essNpc);
            Execution.delay(400, 800);
        }
        
        CExecution.delayUntil(() -> CMouse.fastInteract(essNpc, action2), Random.nextInt(450, 650), 3600, 4000);
        
        if (Random.nextInt(5) < 4) {
            CMouse.moveRandomFromPosition();
        }

        if (Execution.delayUntil(() -> Players.getLocal().getAnimationId() != -1 || Players.getLocal().isMoving(), 2200, 2900)) {
            Execution.delayWhile(() -> Players.getLocal().isMoving());
            Execution.delay(2000, 3000);
            if (Players.getLocal().getAnimationId() == -1) {
                essNpc = null;
            }
        }
    }
}
