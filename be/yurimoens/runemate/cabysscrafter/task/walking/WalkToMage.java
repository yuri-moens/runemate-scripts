package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.Location;
import com.runemate.game.api.hybrid.entities.Actor;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

class WalkToMage extends Task {

    private final SlotAction surge;
    private final SlotAction anticipation;

    public WalkToMage() {
        surge = ActionBar.getFirstAction("Surge");
        anticipation = ActionBar.getFirstAction("Anticipation");
    }

    @Override
    public boolean validate() {
        Player player = Players.getLocal();

        return (getParent().validate()
                && !((WalkToAbyss) getParent()).underAttack
                && (Location.getLocation() == Location.WILDERNESS || player.getAnimationId() == 18224));
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();

        if (surge != null && !surge.isCoolingDown()) {
            surge.activate(false);
            Execution.delayUntil(surge::isCoolingDown, 1000);
        }

        surge();

        PredefinedPath path = PredefinedPath.create(
                new Coordinate(3103, 3545, 0).randomize(2, 2),
                new Coordinate(3102, 3558, 0).randomize(2, 2)
        );

        Execution.delayUntil(() -> {
            if (!player.isMoving() || player.distanceTo(path.getNext()) < 17.5D) {
                path.step(true);
                Execution.delayUntil(player::isMoving, 1500, 2500);
            }

            for (Player otherPlayers : Players.getLoaded()) {
                Actor target;
                if ((target = otherPlayers.getTarget()) != null && target.getName() != null && target.getName().equals(player.getName())) {
                    ((WalkToAbyss) getParent()).underAttack = true;
                    return false;
                }
            }

            Npc zamorakMage = Npcs.newQuery().models(Constants.MAGE_MODEL).results().first();

            return Constants.mageArea.contains(player)
                    || (zamorakMage != null && zamorakMage.isVisible());
        }, 10000, 13000);
    }

    private Future<Boolean> surge() {
        if (surge == null || surge.isCoolingDown() || Players.getLocal().getPosition().getX() == 3093) {
            return new FutureTask<>(() -> false);
        }

        FutureTask task = new FutureTask(() -> surge.activate(false));
        FutureTask task2;

        if (anticipation != null) {
            task2 = new FutureTask(() -> anticipation.activate(false));
        } else {
            task2 = new FutureTask<>(() -> false);
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(task, task2);

        return task;
    }
}
