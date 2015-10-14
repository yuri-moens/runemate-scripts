package be.yurimoens.runemate.cabysscrafter.task.summoning;

import be.yurimoens.runemate.cabysscrafter.Location;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class SummonFamiliar extends Task {

    @Override
    public boolean validate() {
        return Summoning.getMinutesRemaining() <= 2 && Summoning.getPoints() > 10 && Location.getLocation() == Location.TZHAAR_CAVES;
    }

    @Override
    public void execute() {
        SlotAction summonAction = ActionBar.getFirstAction("Abyssal titan pouch");

        if (!Inventory.contains(12796)) {
            Bank.open();

            if (!Bank.isOpen()) {
                return;
            }

            Bank.getItems(12796).first().click();

            Execution.delay(200, 400);

            Bank.close();

            if (!Execution.delayUntil(() -> summonAction.isActivatable() && summonAction.isReady(), 3000, 4000)) {
                return;
            }
        }

        if (Players.getLocal().getFamiliar() != null || Summoning.getMinutesRemaining() >= 1) {
            Interfaces.getAt(1430, 19).click();
        } else {
            if (summonAction.isActivatable() && summonAction.isReady()) {
                summonAction.activate(false);
            }
        }
    }
}
