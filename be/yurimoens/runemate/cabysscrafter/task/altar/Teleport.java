package be.yurimoens.runemate.cabysscrafter.task.altar;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CMouse;
import be.yurimoens.runemate.util.CSpriteItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class Teleport extends Task {

    @Override
    public boolean validate() {
        return (getParent().validate()
                && !Inventory.contains(Constants.PURE_ESSENCE));
    }

    @Override
    public void execute() {
        Execution.delay(400, 900);

        if (Equipment.getItemIn(Equipment.Slot.NECK) == null) {
            Inventory.getItems(new Filter<SpriteItem>() {
                @Override
                public boolean accepts(SpriteItem spriteItem) {
                    return CSpriteItem.getName(spriteItem).startsWith("Amulet");
                }
            }).first().click();

            Execution.delayUntil(() -> Equipment.getItemIn(Equipment.Slot.NECK) != null, 3000, 4000);
        }

        while (!Equipment.getItemIn(Equipment.Slot.NECK).interact("Edgeville")) {
            Execution.delay(600, 1200);
        }

        CMouse.moveToMinimap();

        Execution.delayUntil(() -> Players.getLocal().getPosition().equals(Constants.edgevilleTeleport) || Equipment.getItemIn(Equipment.Slot.NECK) == null, 7000, 9000);
    }
}
