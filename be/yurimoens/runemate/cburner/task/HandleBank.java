package be.yurimoens.runemate.cburner.task;

import be.yurimoens.runemate.util.CBank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.framework.task.Task;

public class HandleBank extends Task {

    @Override
    public boolean validate() {
        return !Inventory.contains(1517) || (GameObjects.newQuery().names("Portable brazier").results().isEmpty() && !Inventory.isFull());
    }

    @Override
    public void execute() {
        CBank.open();

        if (!Bank.isOpen()) {
            return;
        }

        Bank.loadPreset(2, true);
    }
}
