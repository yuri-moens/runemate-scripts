package be.yurimoens.runemate.csmelter.task;

import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
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
        GameObject furnace = GameObjects.newQuery().names("Furnace").results().first();

        if (furnace == null) {
            return;
        }

        if (!furnace.isVisible()) {
            Camera.turnTo(furnace, 0.318D);
        }

        if (!CExecution.delayUntil(() -> CMouse.fastInteract(furnace, "Smelt"), Random.nextInt(900, 1300), 5000, 6000)) {
            return;
        }

        if (Execution.delayUntil(player::isMoving, 1800, 2400)) {
            Camera.turnTo(90, 0.318D);
            Execution.delayWhile(player::isMoving);
        }

        Execution.delayUntil(() -> Interfaces.getAt(1371, 0) != null, 1800, 2400);

        CMouse.concurrentlyMove(Banks.getLoaded().nearest());

        if (Interfaces.getAt(1371, 0) != null) {
            Keyboard.type(" ", false);
        }

        Execution.delayUntil(() -> Interfaces.getAt(1251, 0) != null, 4000, 5000);
    }
}
