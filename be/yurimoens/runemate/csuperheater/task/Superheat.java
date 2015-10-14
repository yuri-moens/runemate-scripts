package be.yurimoens.runemate.csuperheater.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Superheat extends Task {

    private final SlotAction superheat;

    public Superheat() {
        superheat = ActionBar.getFirstAction("Superheat Item");
    }

    @Override
    public boolean validate() {
        return Inventory.contains(451);
    }

    @Override
    public void execute() {
        SpriteItem ore = Inventory.getItems("Runite ore").last();

        superheat.activate(false);

        Execution.delay(50, 300);

        ore.click();

        Execution.delay(600, 1200);
    }
}
