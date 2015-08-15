package be.yurimoens.runemate.cabysscrafter.task.bank;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CBank;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.awt.event.KeyEvent;

class WithdrawEssence extends Task {

    private final int PRESET;
    private final String GIANT_POUCH_HOTKEY;
    private final String POUCH_HOTKEYS;

    public WithdrawEssence(int preset) {
        PRESET = preset;

        if (Inventory.contains(Constants.GIANT_POUCH)) {
            GIANT_POUCH_HOTKEY = ActionBar.getFirstAction(Constants.GIANT_POUCH).getSlot().getKeyBind();
        } else {
            GIANT_POUCH_HOTKEY = "";
        }

        String pouchHotkeys = "";
        for (SlotAction slot : ActionBar.getActions(Constants.SMALL_POUCH, Constants.MEDIUM_POUCH, Constants.LARGE_POUCH)) {
            pouchHotkeys += slot.getSlot().getKeyBind();
        }
        POUCH_HOTKEYS = pouchHotkeys;
    }

    @Override
    public boolean validate() {
        return (Bank.isOpen()
                && !Inventory.contains(Constants.PURE_ESSENCE)
                && Equipment.getItemIn(Equipment.Slot.NECK).getId() != Constants.AMULET_OF_GLORY_EMPTY
                && (!Inventory.isFull() || (Inventory.isFull() && Inventory.contains(Constants.AMULET_OF_GLORY_CHARGED))));
    }

    @Override
    public void execute() {
        if (Random.nextInt(2) == 1) {
            openBankAndWithdraw();
            CMouse.concurrentlyMove(Banks.getLoaded().nearest());
        } else {
            CMouse.concurrentlyMove(Banks.getLoaded().nearest());
            openBankAndWithdraw();
        }

        if (!POUCH_HOTKEYS.equals("")) {
            while (Inventory.isFull()) {
                Execution.delay(200, 400);
                Keyboard.type(POUCH_HOTKEYS, false);
                Execution.delayWhile(Inventory::isFull, 1500, 2500);
            }

            openBankAndWithdraw();
        }


        if (!GIANT_POUCH_HOTKEY.equals("")) {
            while (Inventory.isFull()) {
                Execution.delay(200, 400);
                Keyboard.type(GIANT_POUCH_HOTKEY, false);
                Execution.delayWhile(Inventory::isFull, 1500, 2500);
            }

            openBankAndWithdraw();
        }

        Execution.delayUntil(Inventory::isFull, 4000, 6000);
    }

    private void openBankAndWithdraw() {
        CExecution.delayUntil(CBank::open, Random.nextInt(450, 650), 4000, 5000);

        if (Inventory.containsAnyOf(Constants.DEGRADED_POUCH_IDS)) {
            while (!Inventory.isFull()) {
                Bank.withdraw(Constants.PURE_ESSENCE, 0);
                Execution.delayUntil(Inventory::isFull, 1400, 1800);
            }

            Keyboard.typeKey(KeyEvent.VK_ESCAPE);
            Execution.delayWhile(Bank::isOpen, 1500, 2500);
        } else {
            Bank.loadPreset(PRESET, true);
            Execution.delayWhile(Bank::isOpen, 1500, 2500);
        }

        Execution.delayUntil(Inventory::isFull, 2500, 3500);
    }
}
