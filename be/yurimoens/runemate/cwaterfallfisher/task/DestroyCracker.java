package be.yurimoens.runemate.cwaterfallfisher.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class DestroyCracker extends Task {

    private final int PRAWN_CRACKER_ID = 34687;

    @Override
    public boolean validate() {
        return Inventory.contains(PRAWN_CRACKER_ID);
    }

    @Override
    public void execute() {
        SpriteItem cracker = Inventory.getItems(PRAWN_CRACKER_ID).first();

        cracker.interact("Destroy");

        if (!Execution.delayUntil(() -> Interfaces.getAt(1183, 0) != null, 3000, 4000)) {
            return;
        }

        Execution.delay(400, 700);

        InterfaceComponent confirmButton = Interfaces.getAt(1183, 24).isVisible() ? Interfaces.getAt(1183, 24) : Interfaces.getAt(1183, 16);

        confirmButton.click();
    }
}
