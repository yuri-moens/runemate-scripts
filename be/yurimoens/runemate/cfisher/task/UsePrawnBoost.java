package be.yurimoens.runemate.cfisher.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import com.runemate.game.api.script.framework.task.Task;


public class UsePrawnBoost extends Task implements ChatboxListener {

    private boolean canBoost = true;

    @Override
    public boolean validate() {
        return canBoost && Inventory.containsAnyOf(34690, 34689);
    }

    @Override
    public void execute() {
        SpriteItemQueryResults boosters = Inventory.getItems(34690);

        if (boosters.isEmpty()) {
            boosters = Inventory.getItems(34689);
        }

        boosters.first().click();
        canBoost = false;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getMessage().startsWith("You no longer have a boosted chance")) {
            canBoost = true;
        }
    }
}
