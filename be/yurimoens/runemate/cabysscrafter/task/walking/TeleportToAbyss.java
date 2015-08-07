package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CExecution;
import com.runemate.game.api.hybrid.entities.Actor;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class TeleportToAbyss extends Task {

    private Npc zamorakMage;

    @Override
    public boolean validate() {
        zamorakMage = Npcs.newQuery().filter(Npcs.getModelFilter(Constants.MAGE_MODEL)).results().first();
        return (getParent().validate()
                && !((WalkToAbyss) getParent()).underAttack
                && (Constants.mageArea.contains(Players.getLocal()) || (zamorakMage != null && zamorakMage.isVisible())));
    }

    @Override
    public void execute() {
        if (!zamorakMage.isVisible()) {
            Camera.turnTo(zamorakMage, 0.350D);
        }

        for (Player player : Players.getLoaded()) {
            Actor target;
            if ((target = player.getTarget()) != null && target.getName() != null && target.getName().equals(Players.getLocal().getName())) {
                ((WalkToAbyss) getParent()).underAttack = true;
                return;
            }
        }

        CExecution.delayUntil(() -> zamorakMage.interact("Teleport"), Random.nextInt(450, 650), 4000, 5000);

        if (Execution.delayUntil(() -> Players.getLocal().getTarget() != null && Players.getLocal().getTarget().getName() == null, 1000, 1500)) {
            Execution.delayWhile(() -> Players.getLocal().distanceTo(Constants.wildernessWall) < 50D, 4000, 6500);
        }
    }
}
