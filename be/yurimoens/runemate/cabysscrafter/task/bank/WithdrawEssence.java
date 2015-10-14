package be.yurimoens.runemate.cabysscrafter.task.bank;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.Location;
import be.yurimoens.runemate.cabysscrafter.RuneType;
import be.yurimoens.runemate.util.BankWithdrawal;
import be.yurimoens.runemate.util.CBank;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
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

    private final SlotAction repairRunePouch;
    private final SlotAction glorySlot;

    public WithdrawEssence(int preset) {
        PRESET = preset;

        if (Inventory.contains(Constants.GIANT_POUCH)) {
            GIANT_POUCH_HOTKEY = ActionBar.getFirstAction("Giant pouch").getSlot().getKeyBind();
        } else {
            GIANT_POUCH_HOTKEY = "";
        }

        String pouchHotkeys = "";
        for (SlotAction slot : ActionBar.getActions("Large pouch", "Medium pouch", "Small pouch")) {
            pouchHotkeys += slot.getSlot().getKeyBind();
        }
        POUCH_HOTKEYS = pouchHotkeys;

        repairRunePouch = ActionBar.getFirstAction("Repair Rune Pouch");
        glorySlot = ActionBar.getFirstAction("Amulet of glory");
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
            fillPouches(POUCH_HOTKEYS);
        }


        if (!GIANT_POUCH_HOTKEY.equals("")) {
            fillPouches(GIANT_POUCH_HOTKEY);
        }

        Execution.delayUntil(Inventory::isFull, 4000, 6000);

        if (Location.getLocation() == Location.TZHAAR_CAVES) {
            teleportToEdgeville();
        }
    }

    private void fillPouches(String pouchHotkeys) {
        while (Inventory.isFull()) {
            Execution.delay(200, 400);
            Keyboard.type(pouchHotkeys, false);
            Execution.delayWhile(Inventory::isFull, 1500, 2500);
        }

        if (repairRunePouch != null && Inventory.containsAnyOf(Constants.DEGRADED_POUCH_IDS)) {
            repairPouch();
        }

        openBankAndWithdraw();
    }

    private void openBankAndWithdraw() {
        CExecution.delayUntil(CBank::open, Random.nextInt(450, 650), 4000, 5000);

        if (Interfaces.getAt(671, 21) != null) {
            Keyboard.typeKey(KeyEvent.VK_ESCAPE);
            openBankAndWithdraw();
            return;
        }

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

    private void repairPouch() {
        CExecution.delayUntil(CBank::open, Random.nextInt(450, 650), 4000, 5000);

        if (!CBank.ensureWithdrawals(
                new BankWithdrawal(RuneType.COSMIC.RUNE_ID, 1),
                new BankWithdrawal(9075, 1),
                new BankWithdrawal(RuneType.LAW.RUNE_ID, 1)
        )) return;

        CExecution.delayUntil(Bank::close, Random.nextInt(750, 950), 4000, 5000);

        Execution.delay(800, 1300);

        ActionBar.getFirstAction("Repair Rune Pouch").activate(false);
        Execution.delay(650, 950);
        Inventory.getItems(Constants.DEGRADED_POUCH_IDS).first().click();

        Execution.delay(750, 950);
    }

//    private void repairPouch() {
//        CExecution.delayUntil(CBank::open, Random.nextInt(450, 650), 4000, 5000);
//
//        CExecution.delayUntil(() -> {
//            int itemCount = Inventory.getQuantity(RuneType.COSMIC.RUNE_ID);
//            Bank.getItems(RuneType.COSMIC.RUNE_ID).first().click();
//
//            Execution.delayUntil(() -> itemCount < Inventory.getQuantity(RuneType.COSMIC.RUNE_ID), 1000, 1200);
//
//            return Inventory.contains(RuneType.COSMIC.RUNE_ID);
//        }, Random.nextInt(450, 750), 4000, 5000);
//
//        CExecution.delayUntil(() -> {
//            int itemCount = Inventory.getQuantity(9075);
//            Bank.getItems(9075).first().click();
//
//            Execution.delayUntil(() -> itemCount < Inventory.getQuantity(9075), 1000, 1200);
//
//            return Inventory.getQuantity(9075) == 2;
//        }, Random.nextInt(450, 750), 4000, 5000);
//
//        CExecution.delayUntil(() -> {
//            int itemCount = Inventory.getQuantity(RuneType.LAW.RUNE_ID);
//            Bank.getItems(RuneType.LAW.RUNE_ID).first().click();
//
//            Execution.delayUntil(() -> itemCount < Inventory.getQuantity(RuneType.LAW.RUNE_ID), 1000, 1200);
//
//            return Inventory.contains(RuneType.LAW.RUNE_ID);
//        }, Random.nextInt(450, 750), 4000, 5000);
//
//        Execution.delay(650, 900);
//
//        CExecution.delayUntil(Bank::close, Random.nextInt(750, 950), 4000, 5000);
//
//        Execution.delay(800, 1300);
//
//        ActionBar.getFirstAction("Repair Rune Pouch").activate(false);
//        Execution.delay(650, 950);
//        Inventory.getItems(Constants.DEGRADED_POUCH_IDS).first().click();
//
//        Execution.delay(750, 950);
//    }

    private void teleportToEdgeville() {
        Execution.delay(350, 750);

        CExecution.delayWhile(() -> {
            glorySlot.activate(false);

            return Interfaces.getAt(Constants.TELEPORT_INTERFACE, 0) == null;
        }, Random.nextInt(520, 870), 3000, 4000);

        if (Interfaces.getAt(Constants.TELEPORT_INTERFACE, 0) != null) {
            Keyboard.type("1", false);
        }

        Execution.delayUntil(() -> Location.getLocation() == Location.EDGEVILLE, 7000, 9000);
    }
}
