package be.yurimoens.runemate.csmelter.task;

import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.awt.event.KeyEvent;

public class HandleBank extends Task {

    @Override
    public boolean validate() {
        return !Inventory.contains(444);
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();

        LocatableEntity bank = Banks.getLoaded().nearest();

        if (!bank.isVisible()) {
            Camera.turnTo(bank, 0.318D);
        }

        if (!CExecution.delayUntil(() -> CMouse.fastInteract(bank, "Bank"), Random.nextInt(900, 1200), 4000, 5000));

        if (Execution.delayUntil(player::isMoving, 1800, 2400)) {
            Camera.turnTo(294, 0.318D);
            Execution.delayWhile(player::isMoving);
        }

        CMouse.concurrentlyMove(GameObjects.newQuery().names("Furnace").results().first());

        if (Interfaces.getAt(205, 0) != null) {
            Keyboard.typeKey(KeyEvent.VK_ESCAPE);
            return;
        }

        if (!Execution.delayUntil(Bank::isOpen, 800, 1200)) {
            return;
        }

        Bank.loadPreset(1, true);

        Execution.delayUntil(() -> Inventory.contains(444), 2000, 3000);
    }
}
