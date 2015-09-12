package be.yurimoens.runemate.cgraniteminer.task;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class MineGranite extends Task {

    private LocatableEntityQueryResults<GameObject> rocks;
    private GameObject lastRock;
    private Timer timeout;

    public MineGranite() {
        timeout = new Timer(5000L);
        timeout.start();
    }

    @Override
    public boolean validate() {
        if (lastRock == null || !lastRock.isValid() || timeout.getRemainingTime() == 0) {
            if (!Inventory.isFull() && Players.getLocal().getAnimationId() == -1) {
                rocks = GameObjects.newQuery().filter((gameObject) ->
                    gameObject.getId() == 10947 && gameObject.getPosition().getX() != 3164 && gameObject.distanceTo(Players.getLocal()) < 4
                ).results();
                return !rocks.isEmpty();
            }
        }

        if (Players.getLocal().isMoving() || Players.getLocal().getAnimationId() != -1) {
            timeout.reset();
        }

        return false;
    }

    @Override
    public void execute() {
        lastRock = rocks.nearest();
        while (!lastRock.click()) {
            Execution.delay(200, 500);
        }
        timeout.reset();
    }
}
