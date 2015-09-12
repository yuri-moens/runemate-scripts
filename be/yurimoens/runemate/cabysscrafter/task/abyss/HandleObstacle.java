package be.yurimoens.runemate.cabysscrafter.task.abyss;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.Actor;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import com.runemate.game.api.script.framework.task.Task;

import java.util.ArrayList;
import java.util.List;

class HandleObstacle extends Task implements ChatboxListener {

    private final int RIFT_ID;
    private final String[] IGNORE_OBSTACLES;

    private Timer timeout;
    private boolean passedObstacle = false;

    public HandleObstacle(int riftId, String[] ignoreObstacles) {
        RIFT_ID = riftId;
        IGNORE_OBSTACLES = ignoreObstacles;
    }

    @Override
    public boolean validate() {
        return (getParent().validate()
                && !Constants.innerRing.contains(Players.getLocal())
                && !getObstacles().isEmpty());
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();
        GameObject nearest;
        LocatableEntityQueryResults<GameObject> sortedObstacles = getObstacles().sortByDistance();

        if (sortedObstacles.get(1).distanceTo(Players.getLocal()) - sortedObstacles.get(0).distanceTo(Players.getLocal()) < 5D) {
            List<GameObject> firstTwoList = new ArrayList<>();
            firstTwoList.add(sortedObstacles.get(0));
            firstTwoList.add(sortedObstacles.get(1));

            LocatableEntityQueryResults<GameObject> firstTwo = new LocatableEntityQueryResults<>(firstTwoList);

            nearest = firstTwo.nearestTo(GameObjects.newQuery().filter((gameObject) -> gameObject.getId() == RIFT_ID).results().first());
        } else {
            nearest = sortedObstacles.nearest();
        }

        Execution.delay(300, 900);

        if (!nearest.isVisible()) {
            PredefinedPath.create(nearest.getPosition().randomize(2, 2)).step(true);

            if (Execution.delayUntil(player::isMoving, 2000, 3000)) {
                Execution.delayUntil(nearest::isVisible, 5000, 7000);
            } else {
                return;
            }
        }

        CExecution.delayUntil(() -> CMouse.fastInteract(nearest, getAction(nearest)), Random.nextInt(650, 850), 3600, 4000);

        if (Camera.getPitch() < 0.600D) {
            Camera.concurrentlyTurnTo(320, 0.666D, 0.08);
        }

        timeout = new Timer(Random.nextInt(11000, 14000));
        timeout.start();
        while (timeout.isRunning() && !isFighting()) {
            Execution.delay(200, 400);
        }

        if (passedObstacle) {
            passedObstacle = false;
            Execution.delayUntil(() -> Constants.innerRing.contains(Players.getLocal()), 5000, 7000);
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getMessage().startsWith("...and")) {
            passedObstacle = true;
            if (timeout != null) {
                timeout.stop();
            }
        } else if (messageEvent.getMessage().startsWith("You fail") || messageEvent.getMessage().startsWith("You are not")) {
            if (timeout != null) {
                timeout.stop();
            }
        }
    }

    private LocatableEntityQueryResults<GameObject> getObstacles() {
        return GameObjects.newQuery().filter((GameObject gameObject) -> {
            for (int obstacleId : Constants.OBSTACLES) {
                if (obstacleId == gameObject.getId()) {
                    String obstacleName = gameObject.getDefinition().getLocalState().getName();

                    if (obstacleName.equals("Blockage")) {
                        return false;
                    }

                    for (String ignoreObstacle : IGNORE_OBSTACLES) {
                        if (ignoreObstacle.equals(obstacleName)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }).results();
    }

    private boolean isFighting() {
        Actor actor = Players.getLocal().getTarget();

        return actor != null && actor.getName().startsWith("Abyssal");
    }

    private String getAction(GameObject obstacle) {
        String action;

        switch (obstacle.getDefinition().getLocalState().getName()) {
            case "Rock": action = "Mine"; break;
            case "Gap": action = "Squeeze-through"; break;
            case "Tendrils": action = "Chop"; break;
            case "Eyes": action = "Distract"; break;
            case "Boil": action = "Burn-down"; break;
            case "Passage": action = "Go-through"; break;
            default: action = "";
        }

        return action;
    }
}
