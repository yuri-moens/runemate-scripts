package be.yurimoens.runemate.cdivination.task.harvest;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class Harvest extends Task {

    public Harvest() {
        add(new HarvestEnrichedWisp(), new HarvestWisp(), new FocusNext());
    }

    @Override
    public boolean validate() {
        return (!Inventory.isFull()
                && !Players.getLocal().isMoving());
    }

    @Override
    public void execute() {

    }
}
