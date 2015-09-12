package be.yurimoens.runemate.cfletcher.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class StringBows extends Task {

    private final SlotAction actionSlot;

    public StringBows() {
        actionSlot = ActionBar.getFirstAction(1777);
    }

    @Override
    public boolean validate() {
        return (!Inventory.getItems(1777).isEmpty() && Interfaces.getAt(1251, 0) == null);
    }

    @Override
    public void execute() {
        if (actionSlot == null) {
            Inventory.getItems(1777).first().click();
        } else {
            actionSlot.activate(false);
        }

        Execution.delay(0, 350);

        Timer timeout = new Timer(2000L);
        timeout.start();
        boolean interfaceFound = false;
        while (timeout.getRemainingTime() != 0 && !interfaceFound) {
            if (Interfaces.getAt(1371, 0) != null) {
                interfaceFound = true;
                break;
            }
        }

        Execution.delay(0, 350);

        if (interfaceFound) {
            Keyboard.type(" ", false);
        }
    }
}
