package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

public class CBank {

    public static boolean open() {
        return open(Banks.getLoaded().nearest(), "Bank");
    }

    public static boolean open(LocatableEntity bank, String action) {
        Player player;
        return Bank.isOpen() || (bank != null && bank.isVisible() && CMouse.fastInteract(bank, action) && (player = Players.getLocal()) != null && Execution.delayUntil(Bank::isOpen, player::isMoving, 1500, 2500));
    }

    public static boolean ensureWithdrawals(BankWithdrawal... withdrawals) {
        if (withdrawals == null || !Bank.isOpen()) {
            return false;
        }

        for (BankWithdrawal withdrawal : withdrawals) {
            if (withdrawal.ID == -1) {
                withdrawal.setExpectedAmount(Inventory.getQuantity(withdrawal.NAME));
            } else {
                withdrawal.setExpectedAmount(Inventory.getQuantity(withdrawal.ID));
            }
        }

        return CExecution.delayUntil(() -> {
            boolean finished = true;

            for (BankWithdrawal withdrawal : withdrawals) {
                if (!withdrawal.isFinished()) {
                    if (!withdrawal.withdraw()) {
                        return false;
                    }

                    finished = false;
                    Execution.delay(0, 250);
                }
            }

            return finished;
        }, Random.nextInt(800, 1200), withdrawals.length * 7000, withdrawals.length * 7000 + 3000);
    }

}
