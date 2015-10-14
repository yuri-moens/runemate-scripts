package be.yurimoens.runemate.cabysscrafter.task.altar;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.Location;
import be.yurimoens.runemate.util.CExecution;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class Teleport extends Task {

    private final SlotAction glorySlot; // Glory Hole! Where strangers become friends! Glory Hole! There's no need to know names.
    private final SlotAction tokkulZoSlot;

    public Teleport() {
        glorySlot = ActionBar.getFirstAction("Amulet of glory");
        tokkulZoSlot = ActionBar.getFirstAction("TokKul-Zo (Charged)");
    }

    @Override
    public boolean validate() {
        return (getParent().validate()
                && !Inventory.contains(Constants.PURE_ESSENCE));
    }

    @Override
    public void execute() {
        Execution.delay(350, 750);

        if (tokkulZoSlot == null) {
            glorySlot.activate(false);
        } else {
            tokkulZoSlot.activate(false);
        }

        if (!Execution.delayWhile(() -> Interfaces.getAt(Constants.TELEPORT_INTERFACE, 0) == null, 3000, 4000)) {
            return;
        }

        if (Interfaces.getAt(Constants.TELEPORT_INTERFACE, 0) != null) {
            if (tokkulZoSlot == null) {
                Keyboard.type("1", false);
            } else {
                if (Summoning.getPoints() <= 10 && Summoning.getMinutesRemaining() <= 2) {
                    Keyboard.type("1", false);
                } else {
                    Keyboard.type("4", false);
                }
            }
        }

        Execution.delayUntil(() -> Location.getLocation() == Location.EDGEVILLE || Location.getLocation() == Location.TZHAAR_CAVES || Location.getLocation() == Location.TZHAAR_PLAZA, 7000, 9000);
    }
}
