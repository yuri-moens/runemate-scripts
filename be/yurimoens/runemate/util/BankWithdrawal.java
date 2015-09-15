package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;

public class BankWithdrawal {

    public final int ID;
    public final int AMOUNT;
    public final String NAME;

    private int expectedAmount;

    public BankWithdrawal(int id, int amount) {
        this.ID = id;
        this.AMOUNT = amount;
        this.NAME = null;
    }

    public BankWithdrawal(String name, int amount) {
        this.NAME = name;
        this.AMOUNT = amount;
        this.ID = -1;
    }

    public void setExpectedAmount(int startAmount) {
        this.expectedAmount = startAmount + AMOUNT;
    }

    public int getExpectedAmount() {
        return this.expectedAmount;
    }

    public int getCurrentAmount() {
        if (ID == -1) {
            return Inventory.getQuantity(NAME);
        } else {
            return Inventory.getQuantity(ID);
        }
    }

    public boolean isFinished() {
        return getCurrentAmount() == getExpectedAmount();
    }

    public boolean withdraw() {
        if (ID == -1) {
            if (Bank.getItems(NAME).isEmpty()) {
                return false;
            }

            if (AMOUNT == 1) {
                Bank.getItems(NAME).first().click();
            } else {
                Bank.withdraw(NAME, AMOUNT);
            }
        } else {
            if (Bank.getItems(ID).isEmpty()) {
                return false;
            }

            if (AMOUNT == 1) {
                Bank.getItems(ID).first().click();
            } else {
                Bank.withdraw(ID, AMOUNT);
            }
        }

        return true;
    }

}
