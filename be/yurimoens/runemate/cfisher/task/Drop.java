package be.yurimoens.runemate.cfisher.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.ArrayList;
import java.util.Collections;

public class Drop extends Task {

    private final int[] FISH_IDS = { 331, 335, 11328, 11330, 11332 };
    private ArrayList<SlotAction> slotActions;

    public Drop() {
        slotActions = new ArrayList<>();
        slotActions.add(ActionBar.getFirstAction(11328));
        slotActions.add(ActionBar.getFirstAction(11330));
        slotActions.add(ActionBar.getFirstAction(11332));
    }

    @Override
    public boolean validate() {
        return Inventory.isFull();
    }

    @Override
    public void execute() {
        Execution.delayUntil(() -> ChatDialog.getContinue() != null);

        while (ChatDialog.getContinue() != null) {
            Keyboard.type(" ", false);
            Execution.delay(300, 600);
        }

        Timer timeout = new Timer(8000L);
        timeout.start();
        while (timeout.isRunning() && Inventory.containsAnyOf(FISH_IDS)) {
            Collections.shuffle(slotActions);
            slotActions.stream().forEach((slot) -> slot.activate(false));
        }
    }
}
