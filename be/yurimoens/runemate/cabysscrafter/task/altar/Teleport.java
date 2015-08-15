package be.yurimoens.runemate.cabysscrafter.task.altar;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CExecution;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class Teleport extends Task {

    private final SlotAction glorySlot; // Glory Hole! Where strangers become friends! Glory Hole! There's no need to know names.

    public Teleport() {
        glorySlot = ActionBar.getFirstAction("Amulet of glory");
    }

    @Override
    public boolean validate() {
        return (getParent().validate()
                && !Inventory.contains(Constants.PURE_ESSENCE));
    }

    @Override
    public void execute() {
        Execution.delay(350, 750);

        CExecution.delayWhile(() -> {
            glorySlot.activate(false);

            return Interfaces.getAt(Constants.GLORY_TELEPORT_INTERFACE, 0) == null;
        }, Random.nextInt(520, 870), 3000, 4000);

        Keyboard.type("1", false);

        Execution.delayUntil(() -> Players.getLocal().getPosition().equals(Constants.edgevilleTeleport), 7000, 9000);
    }
}
