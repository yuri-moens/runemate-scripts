package be.yurimoens.runemate.csupermake.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class SuperMake extends Task {

    private final SlotAction slot;

    public SuperMake() {
        slot = ActionBar.getFirstAction(2006);
    }

    @Override
    public boolean validate() {
        return (Inventory.contains(1783));
    }

    @Override
    public void execute() {
        Keyboard.type("2", false);

        Execution.delay(1400, 1900);
    }
}
