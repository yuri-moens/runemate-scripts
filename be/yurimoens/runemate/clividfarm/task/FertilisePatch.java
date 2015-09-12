package be.yurimoens.runemate.clividfarm.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class FertilisePatch extends Task {

    private LocatableEntityQueryResults<GameObject> emptyPatches;
    private GameObject emptyPatch;
    private Timer timeout;

    @Override
    public boolean validate() {
        return !(emptyPatches = GameObjects.newQuery().names("Empty patch").filter((GameObject go) -> {
            if (emptyPatch != null) {
                return !go.getPosition().equals(emptyPatch.getPosition());
            } else {
                return true;
            }
        }).results()).isEmpty();
    }

    @Override
    public void execute() {
        emptyPatch = emptyPatches.nearest();

        if (!emptyPatch.isVisible()) {
            Camera.turnTo(emptyPatch, 0.666D);
        }

        if (timeout != null && timeout.isRunning()) {
            emptyPatch.hover();

            Execution.delayWhile(timeout::isRunning);
        }

        CMouse.fastInteract(emptyPatch, "Fertilise");

        timeout = new Timer(Random.nextInt(1350, 1800));
        timeout.start();

        if (GameObjects.newQuery().names("Empty patch").results().size() == 1) {
            Execution.delay(950, 1400);
        }
    }
}
