package be.yurimoens.runemate.csuperheater.task;

import be.yurimoens.runemate.util.CBank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.task.Task;

public class HandleBank extends Task {

    @Override
    public boolean validate() {
        return !Inventory.contains(451);
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
