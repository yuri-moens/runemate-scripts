package be.yurimoens.runemate.cdivination.task;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class ConvertMemories extends Task {

    private final String ACTION;

    public ConvertMemories(String action) {
        ACTION = "Convert to " + action;
    }

    @Override
    public boolean validate() {
        return (Inventory.isFull());
    }

    @Override
    public void execute() {
        GameObject rift = GameObjects.newQuery().names("Energy rift").results().first();

        if (Players.getLocal().distanceTo(rift) >= 7D) {
            BresenhamPath path = BresenhamPath.buildTo(rift);
            if (path == null) {
                return;
            }
            path.step(true);
        }

        if (!rift.isVisible()) {
            Camera.concurrentlyTurnTo(rift);
        }

        Execution.delayUntil(() -> rift.isVisible(), 4000, 5000);

        while (!rift.interact(ACTION)) {
            Execution.delay(900, 1800);
        }

        Execution.delay(1200, 1500);

        if (Execution.delayUntil(() -> Players.getLocal().getAnimationId() != -1, 4000, 5000)) {
            Execution.delayWhile(() -> Inventory.contains(new Filter<SpriteItem>() {
                @Override
                public boolean accepts(SpriteItem spriteItem) {
                    return spriteItem.getDefinition().getName().endsWith("memory");
                }
            }));
        }
    }
}
