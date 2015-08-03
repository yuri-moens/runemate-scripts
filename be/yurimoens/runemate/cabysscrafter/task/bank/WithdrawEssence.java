package be.yurimoens.runemate.cabysscrafter.task.bank;

import be.yurimoens.runemate.cabysscrafter.Constants;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Equipment;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class WithdrawEssence extends Task {

    private final int PRESET;
    private final int RUNE_ID;

    public WithdrawEssence(int preset, int runeId) {
        PRESET = preset;
        RUNE_ID = runeId;
    }

    @Override
    public boolean validate() {
        return (Bank.isOpen()
                && !Inventory.contains(Constants.PURE_ESSENCE)
                && Equipment.getItemIn(Equipment.Slot.NECK).getId() != Constants.AMULET_OF_GLORY_EMPTY
                && !Inventory.isFull());
    }

    @Override
    public void execute() {
        Inventory.getItems(Constants.POUCH_IDS).forEach((pouch) -> {
            while (!pouch.interact("Fill")) {
                Execution.delay(900, 1300);
            }
        });

        if (PRESET > 0 && !Inventory.containsAnyOf(Constants.DEGRADED_POUCH_IDS)) {
            Bank.loadPreset(PRESET, true);
        } else {
            if (Inventory.contains(RUNE_ID)) {
                while (!Bank.deposit(RUNE_ID, 0)) {
                    Execution.delay(900, 1200);
                }
            }

            Execution.delay(200, 450);

            if (Bank.containsAnyOf(Constants.PURE_ESSENCE)) {
                while (!Bank.withdraw(Constants.PURE_ESSENCE, 0)) {
                    Execution.delay(900, 1200);
                }
            }
        }

        Execution.delayUntil(() -> Inventory.contains(Constants.PURE_ESSENCE), 4000, 6000);
    }
}
