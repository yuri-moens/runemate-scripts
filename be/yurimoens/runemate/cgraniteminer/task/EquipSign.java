package be.yurimoens.runemate.cgraniteminer.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.script.framework.task.Task;

public class EquipSign extends Task {

    private SpriteItemQueryResults porters;

    @Override
    public boolean validate() {
        SpriteItem pocketItem;
        porters = Inventory.getItems("Sign of the porter IV");

        if ((pocketItem = Equipment.getItemIn(Equipment.Slot.POCKET)) != null) {
            return (!pocketItem.toString().contains("Sign of the porter") && !porters.isEmpty());
        } else {
            return !porters.isEmpty();
        }
    }

    @Override
    public void execute() {
        porters.first().click();
    }
}
