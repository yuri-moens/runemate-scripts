package be.yurimoens.runemate.cfletcher.task;

import be.yurimoens.runemate.cfletcher.event.HandleBankEvent;
import be.yurimoens.runemate.cfletcher.event.HandleBankListener;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.script.framework.task.Task;

import java.util.concurrent.CopyOnWriteArrayList;

public class HandleBank extends Task {

    private final CopyOnWriteArrayList<HandleBankListener> listeners;

    public HandleBank() {
        this.listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public boolean validate() {
        return Inventory.getItems(new Filter<SpriteItem>() {
            @Override
            public boolean accepts(SpriteItem spriteItem) {
                return spriteItem.getId() == 1777;
            }
        }).isEmpty();
    }

    @Override
    public void execute() {
        fireHandleBankEvent();

        if (Bank.open()) {
            Bank.loadPreset(1, true);
        }
    }

    public void addHandleBankListener(HandleBankListener listener) {
        this.listeners.add(listener);
    }

    public void removeHandleBankListener(HandleBankListener listener) {
        this.listeners.remove(listener);
    }

    protected void fireHandleBankEvent() {
        HandleBankEvent event = new HandleBankEvent(this, getBowsStrung(), getLogsCut());

        for (HandleBankListener listener : listeners) {
            listener.handleBankEventReceived(event);
        }
    }

    private int getBowsStrung() {
        return Inventory.getQuantity(new Filter<SpriteItem>() {
            @Override
            public boolean accepts(SpriteItem spriteItem) {
                return spriteItem.getDefinition().getName().endsWith("bow");
            }
        });
    }

    private int getLogsCut() {
        return Inventory.getQuantity(new Filter<SpriteItem>() {
            @Override
            public boolean accepts(SpriteItem spriteItem) {
                return spriteItem.getDefinition().getName().endsWith("bow (u)");
            }
        });
    }
}
