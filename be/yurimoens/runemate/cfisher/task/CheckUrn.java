package be.yurimoens.runemate.cfisher.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class CheckUrn extends Task {

    private final int[] FULL_URNS = { 20336 };

    @Override
    public boolean validate() {
        return Inventory.containsAnyOf(FULL_URNS);
    }

    @Override
    public void execute() {
        Inventory.getItems(FULL_URNS).first().click();

        Timer timeout = new Timer(3000L);
        timeout.start();
        while (timeout.isRunning() && Interfaces.getAt(1371, 0) == null) {
            Execution.delay(100, 250);
        }

        if (Interfaces.getAt(1371, 0) != null) {
            Keyboard.type(" ", false);
        }
    }
}
