package be.yurimoens.runemate.ccutter.task;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class PickupNest extends Task {

    private GroundItem nest;

    @Override
    public boolean validate() {
        return (nest = GroundItems.newQuery().names("Crystal geode").results().nearest()) != null;
    }

    @Override
    public void execute() {
        int count = Inventory.getQuantity("Crystal geode");

        nest.interact("Take");

        Execution.delayUntil(() -> Inventory.getQuantity("Crystal geode") > count, 4000, 5000);
    }
}
