package be.yurimoens.runemate.ccitadel.task;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.task.Task;

public class Mine extends Task {

    private final int PRECIOUS_ORE_ID = 25064;
    private LocatableEntityQueryResults<GameObject> preciousOres;

    @Override
    public boolean validate() {
        preciousOres = GameObjects.newQuery()
                .filter((GameObject gameObject) -> {
                    if (gameObject.getId() == PRECIOUS_ORE_ID) {
                        boolean hasAction = false;
                        for (String action : gameObject.getDefinition().getLocalState().getActions()) {
                            if (action.equals("Mine")) {
                                hasAction = true;
                                break;
                            }
                        }
                        return hasAction;
                    } else {
                        return false;
                    }
                })
                .results();

        return (!preciousOres.isEmpty() && !isAnimating(Random.nextInt(900, 1200)));
    }

    @Override
    public void execute() {
        System.out.println("should mine");
    }

    private boolean isAnimating(int timeoutTime) {
        Timer timeout = new Timer(timeoutTime);
        timeout.start();

        boolean isAnimating = false;
        while (timeout.isRunning() && !isAnimating) {
            isAnimating = (Players.getLocal().getAnimationId() != -1);
        }

        return isAnimating;
    }
}
