package be.yurimoens.runemate.cgraniteminer.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.queries.results.SpriteItemQueryResults;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.script.framework.task.Task;

public class DropGranite extends Task {

    private SpriteItemQueryResults graniteItems;

    @Override
    public boolean validate() {
        graniteItems = Inventory.getItems(new Filter<SpriteItem>() {
            @Override
            public boolean accepts(SpriteItem spriteItem) {
                return spriteItem.toString().contains("Granite");
            }
        });

        return !graniteItems.isEmpty();
    }

    @Override
    public void execute() {
        for (SpriteItem item : graniteItems) {
            item.interact("Drop");
        }
    }
}
