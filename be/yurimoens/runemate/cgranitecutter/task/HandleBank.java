package be.yurimoens.runemate.cgranitecutter.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.task.Task;

public class HandleBank extends Task {

    @Override
    public boolean validate() {
        return Inventory.isFull();
    }

    @Override
    public void execute() {
        if (Bank.open()) {
            Bank.loadPreset(1, true);
        }
    }
}
