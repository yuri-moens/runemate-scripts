package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import com.runemate.game.api.hybrid.entities.Actor;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.web.WebPath;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class WalkToMage extends Task {

    private final SlotAction surge;

    public WalkToMage() {
        surge = ActionBar.getFirstAction("Surge");
    }

    @Override
    public boolean validate() {
        return (getParent().validate()
                && !((WalkToAbyss) getParent()).underAttack
                && Players.getLocal().getPosition().getY() >= Constants.wildernessWall.getY()
                && !Constants.mageArea.contains(Players.getLocal()));
    }

    @Override
    public void execute() {
        WebPath path = Traversal.getDefaultWeb().getPathBuilder().buildTo(Constants.mageArea.getCenter().randomize(3, 3));

        Execution.delayUntil(() -> {
            if (path != null && (!Players.getLocal().isMoving() || Players.getLocal().distanceTo(path.getNext()) < 17.5D)) {
                path.step(true);
                Execution.delayUntil(() -> Players.getLocal().isMoving(), 1500, 2500);
            }

            if (surge != null && !surge.isCoolingDown()) {
                surge.activate(false);
                Execution.delayUntil(surge::isCoolingDown, 1000);
            }

            for (Player player : Players.getLoaded()) {
                Actor target;
                if ((target = player.getTarget()) != null && target.getName() != null && target.getName().equals(Players.getLocal().getName())) {
                    ((WalkToAbyss) getParent()).underAttack = true;
                    return false;
                }
            }

            Npc zamorakMage = Npcs.newQuery().models(Constants.MAGE_MODEL).results().first();

            if (zamorakMage != null) {
                Camera.concurrentlyTurnTo(350, 0.300D, 0.10D);
            }
            return Constants.mageArea.contains(Players.getLocal())
                    || (zamorakMage != null && zamorakMage.isVisible());
        }, 10000, 13000);
    }
}
