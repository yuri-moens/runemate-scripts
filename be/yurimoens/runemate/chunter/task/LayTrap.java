package be.yurimoens.runemate.chunter.task;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class LayTrap extends Task {

    private final int MAX_TRAPS;
    private final SlotAction slot;
    private final Coordinate[] spots;

    public LayTrap(int maxTraps) {
        MAX_TRAPS = maxTraps;

        slot = ActionBar.getFirstAction("Box trap");

        Coordinate startPoint = Players.getLocal().getPosition();
        spots = new Coordinate[MAX_TRAPS];
        for (int i = 0; i < MAX_TRAPS; i++) {
            spots[i] = new Coordinate(startPoint.getX() - (i * 1), startPoint.getY(), startPoint.getPlane());
        }
    }

    @Override
    public boolean validate() {
        return GameObjects.newQuery().names("Box trap", "Shaking box").results().size() < MAX_TRAPS;
    }

    @Override
    public void execute() {
        Coordinate location = findFreeSpot();

        if (location == null) {
            return;
        }

        Player player = Players.getLocal();

        if (!player.getPosition().equals(location)) {
            location.click();
        }

        if (Execution.delayUntil(player::isMoving, 1200, 1800)) {
            Execution.delayWhile(player::isMoving);
        }

        if (!Execution.delayUntil(() -> player.getPosition().equals(location), 600, 1200)) {
            return;
        }

        if (slot != null) {
            slot.activate(false);
        } else {
            Inventory.getItems("Box trap").first().click();
        }

        if (Execution.delayUntil(() -> player.getAnimationId() != -1, 1200, 1800)) {
            Execution.delayUntil(() -> player.getAnimationId() == -1);
            Execution.delay(200, 500);
        }
    }

    private Coordinate findFreeSpot() {
        for (Coordinate spot : spots) {
            boolean taken = false;

            for (GameObject gameObject : GameObjects.getLoadedAt(spot)) {
                if (gameObject != null && gameObject.getDefinition().getName().toLowerCase().contains("box")) {
                    taken = true;
                    break;
                }
            }

            if (!taken) {
                return spot;
            }
        }

        return null;
    }
}
