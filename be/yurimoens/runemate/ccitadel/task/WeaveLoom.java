package be.yurimoens.runemate.ccitadel.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class WeaveLoom extends Task {

    private final int LOOM_IDS[] = { 14435, 14477, 14478, 14662 };

    private GameObject loom;

    @Override
    public boolean validate() {
        loom = GameObjects.newQuery().filter((gameObject) -> {
            for (int loomId : LOOM_IDS) {
                if (gameObject.getId() == loomId) {
                    return true;
                }
            }

            return false;
        }).results().nearest();

        return (Players.getLocal().getAnimationId() == -1 && loom != null);
    }

    @Override
    public void execute() {
        Execution.delay(800, 1200);

        Coordinate loomPosition;
        Coordinate playerPosition = Players.getLocal().getPosition();
        Coordinate tempCoordinate;

        if (loom.getArea().contains(tempCoordinate = new Coordinate(playerPosition.getX() + 1, playerPosition.getY()))) {
            loomPosition = tempCoordinate;
        } else if (loom.getArea().contains(tempCoordinate = new Coordinate(playerPosition.getX() - 1, playerPosition.getY()))) {
            loomPosition = tempCoordinate;
        } else {
            loomPosition = loom.getArea().getRandomCoordinate();
        }

        if (!loomPosition.isVisible()) {
            Camera.turnTo(loomPosition, 0.666D);
        }

        CMouse.fastInteract(loomPosition, "Weave");

        Execution.delay(1200, 1800);

        Execution.delayUntil(() -> Players.getLocal().getAnimationId() != -1 || !Players.getLocal().isMoving(), 7000, 9000);
    }
}
