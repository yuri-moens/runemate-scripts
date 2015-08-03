package be.yurimoens.runemate.cabysscrafter.task.bank;

import be.yurimoens.runemate.cabysscrafter.Constants;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.script.framework.task.Task;

class EquipGlory extends Task {

    @Override
    public boolean validate() {
        return (Bank.isOpen()
                && (Equipment.getItemIn(Equipment.Slot.NECK) == null || Equipment.getItemIn(Equipment.Slot.NECK).getId() == Constants.AMULET_OF_GLORY_EMPTY));
    }

    @Override
    public void execute() {
        if (Bank.isOpen()) {
            SpriteItemQueryResults glories = Bank.getItems(Constants.AMULET_OF_GLORY_CHARGED);
            if (!glories.isEmpty()) {
                glories.first().interact("Wear");
            }
        }
    }
}
