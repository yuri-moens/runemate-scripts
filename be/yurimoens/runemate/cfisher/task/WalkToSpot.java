package be.yurimoens.runemate.cfisher.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class WalkToSpot extends Task {

    private final int[] URNS = { 20334, 20335, 20336 };
    private final Coordinate spot = new Coordinate(2502, 3517, 0);
    private final Coordinate barbarianOutpost = new Coordinate(2520, 3571, 0);
    private final PredefinedPath path;

    public WalkToSpot() {
        path = PredefinedPath.create(
                new Coordinate(2511, 3547, 0),
                new Coordinate(2503, 3522, 0),
                new Coordinate(2499, 3512, 0)
        );
    }

    @Override
    public boolean validate() {
        return (Inventory.containsAnyOf(URNS)
                && spot.distanceTo(Players.getLocal()) > 20);
    }

    @Override
    public void execute() {
        Inventory.getItems(new Filter<SpriteItem>() {
            @Override
            public boolean accepts(SpriteItem spriteItem) {
                return spriteItem.getDefinition().getName().contains("Games necklace");
            }
        }).first().interact("Rub");

        Execution.delayUntil(() -> Interfaces.getAt(1578, 0) != null);

        Keyboard.type("2", false);

        Execution.delayUntil(() -> Players.getLocal().getPosition().distanceTo(barbarianOutpost) < 3);

        while (Players.getLocal().distanceTo(spot) > 3D) {
            path.step(true);
        }
    }
}
