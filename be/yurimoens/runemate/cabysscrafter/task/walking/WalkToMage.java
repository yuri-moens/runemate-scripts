package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import com.runemate.game.api.hybrid.entities.Actor;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
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
        Player player = Players.getLocal();

        return (getParent().validate()
                && !((WalkToAbyss) getParent()).underAttack
                && (player.getPosition().getY() >= Constants.wildernessWall.getY() || player.getAnimationId() == 18224)
                && !Constants.mageArea.contains(player));
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();

        if (surge != null && !surge.isCoolingDown()) {
            surge.activate(false);
            Execution.delayUntil(surge::isCoolingDown, 1000);
        }

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
}
