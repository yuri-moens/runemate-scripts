package be.yurimoens.runemate.csmelter.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.Menu;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Smelt extends Task {

    @Override
    public boolean validate() {
        InterfaceComponent productionInterface  = Interfaces.getAt(1251, 0);

        return Inventory.contains(444) && productionInterface == null;
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();
        LocatableEntity bank = Banks.getLoaded().nearest();
        LocatableEntity furnace = GameObjects.newQuery().names("Furnace").results().first();

        CMouse.clickOption(furnace, "Smelt", 0.318D);

        if (Execution.delayUntil(player::isMoving, 1800, 2400)) {
            if (!Menu.isOpen()) {
                Camera.turnTo(90, 0.318D);
            }
        } else {
            return;
        }

        Execution.delayUntil(() -> Interfaces.getAt(1371, 0) != null || ChatDialog.getContinue() != null, 6000, 7000);

        if (ChatDialog.getContinue() != null) {
            return;
        }

        if (Interfaces.getAt(1371, 0) != null) {
            Keyboard.type(" ", false);
        }

        if (Execution.delayUntil(() -> Interfaces.getAt(1251, 0) != null, 4000, 5000)) {
            if (!bank.isVisible()) {
                Camera.turnTo(bank);
            }

            Execution.delay(0, 500);

            if (!CMouse.hoverOption(bank, "Bank")) {
                CMouse.concurrentlyMove(bank);
            }
        }
    }
}
