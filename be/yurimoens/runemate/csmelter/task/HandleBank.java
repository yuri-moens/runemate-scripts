package be.yurimoens.runemate.csmelter.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.Menu;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.AbstractScript;
import com.runemate.game.api.script.framework.task.Task;

import java.awt.event.KeyEvent;

public class HandleBank extends Task {

    private final AbstractScript SCRIPT;

    public HandleBank(AbstractScript script) {
        this.SCRIPT = script;
    }

    @Override
    public boolean validate() {
        return !Inventory.contains(444);
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();
        LocatableEntity bank = Banks.getLoaded().nearest();
        LocatableEntity furnace = GameObjects.newQuery().names("Furnace").results().first();

        CMouse.clickOption(bank, "Bank", 0.318D);

        if (furnace.isVisible()) {
            Execution.delay(0, 500);

            CMouse.hoverOption(furnace, "Smelt");
        }

        if (Execution.delayUntil(player::isMoving, 1800, 2400)) {
            if (!Menu.isOpen()) {
                Camera.turnTo(294, 0.318D);
            }

            Execution.delayWhile(player::isMoving);
        }

        if (!Menu.isOpen() && furnace.isVisible()) {
            CMouse.concurrentlyMove(furnace);
        }

        if (Interfaces.getAt(205, 0) != null) {
            Keyboard.typeKey(KeyEvent.VK_ESCAPE);

            return;
        }

        if (!Execution.delayUntil(Bank::isOpen, 800, 1200)) {
            return;
        }

        if (Bank.containsAnyOf(444)) {
            Bank.loadPreset(1, true);
        } else {
            RuneScape.logout();
            SCRIPT.stop();
        }

        Execution.delayUntil(() -> !Bank.isOpen(), 2000, 3000);
    }
}
