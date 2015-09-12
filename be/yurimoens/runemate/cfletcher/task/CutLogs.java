package be.yurimoens.runemate.cfletcher.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class CutLogs extends Task {

    private final int[] LOGS_IDS = { 1511, 1521, 1519, 1517, 1515, 1513, 29556 };
    private final SlotAction actionSlot;

    public CutLogs() {
        actionSlot = ActionBar.getFirstAction(LOGS_IDS);
    }

    @Override
    public boolean validate() {
        return (!Inventory.getItems(LOGS_IDS).isEmpty()
                && Interfaces.getAt(1251, 0) == null);
    }

    @Override
    public void execute() {
        if (actionSlot == null) {
            Inventory.getItems(LOGS_IDS).first().click();
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
