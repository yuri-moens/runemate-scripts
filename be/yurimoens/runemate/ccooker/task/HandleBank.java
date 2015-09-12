package be.yurimoens.runemate.ccooker.task;

import be.yurimoens.runemate.ccooker.event.HandleBankEvent;
import be.yurimoens.runemate.ccooker.event.HandleBankListener;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.concurrent.CopyOnWriteArrayList;

public class HandleBank extends Task {

    private final int[] RAW_FOOD = { 377, 383 };
    private final int[] COOKED_FOOD = { 379, 385 };
    private final CopyOnWriteArrayList<HandleBankListener> listeners;

    public HandleBank() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public HandleBank(HandleBankListener... listeners) {
        this();
        for (HandleBankListener listener : listeners) {
            this.listeners.add(listener);
        }
    }

    @Override
    public boolean validate() {
        return Inventory.getItems(RAW_FOOD).isEmpty()
                || (GameObjects.newQuery().names("Portable range").results().isEmpty() && Inventory.getQuantity(RAW_FOOD) != 28);
    }

    @Override
    public void execute() {
        fireHandleBankEvent();

        if (Bank.open()) {
            Bank.loadPreset(1, true);
        }

        Timer timeout = new Timer(2000L);
        timeout.start();
        while (timeout.getRemainingTime() != 0 && Bank.isOpen()) {
            Execution.delay(150, 300);
        }
    }

    public void addHandleBankListener(HandleBankListener listener) {
        this.listeners.add(listener);
    }

    public void removeHandleBankListener(HandleBankListener listener) {
        this.listeners.remove(listener);
    }

    protected void fireHandleBankEvent() {
        HandleBankEvent event = new HandleBankEvent(this, getCookedFood());

        for (HandleBankListener listener : listeners) {
            listener.handleBankEventReceived(event);
        }
    }

    private int getCookedFood() {
        return Inventory.getQuantity(COOKED_FOOD);
    }
}
