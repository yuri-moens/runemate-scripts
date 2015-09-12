package be.yurimoens.runemate.csupermake.task;

import be.yurimoens.runemate.util.CBank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class HandleBank extends Task {

    @Override
    public boolean validate() {
        return (!Inventory.contains(1783));
    }

    @Override
    public void execute() {
        if (CBank.open()) {
            Bank.loadPreset(2, true);

            Execution.delayUntil(() -> !Bank.isOpen(), 3000, 4000);
        }
    }
}
