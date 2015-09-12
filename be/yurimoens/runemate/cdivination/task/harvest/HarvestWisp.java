package be.yurimoens.runemate.cdivination.task.harvest;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class HarvestWisp extends Task {

    private LocatableEntityQueryResults<Npc> wisps;

    @Override
    public boolean validate() {
        wisps = Npcs.newQuery().filter(
                (npc) -> npc.getDefinition().getName().endsWith("wisp")
                        || npc.getDefinition().getName().endsWith("spring")).results();

        return (Players.getLocal().getAnimationId() == -1 && !wisps.isEmpty());
    }

    @Override
    public void execute() {
        GameObject rift = GameObjects.newQuery().names("Energy rift").results().first();
        Npc wisp;

        if (rift != null) {
            int amountOfWisps = Random.nextInt(4, 6);
            LocatableEntityQueryResults<Npc> sortedWisps = wisps.sortByDistanceFrom(rift);

            amountOfWisps = amountOfWisps > sortedWisps.size() ? sortedWisps.size() : amountOfWisps;

            wisp = sortedWisps.limit(amountOfWisps).nearest();
        } else {
            wisp = wisps.nearest();
        }

        if (!wisp.isVisible()) {
            Camera.turnTo(wisp);
        }

        CMouse.fastInteract(wisp, "Harvest");

        Execution.delayUntil(() -> Players.getLocal().isMoving() || Players.getLocal().getAnimationId() != -1, 1200, 1800);
    }
}
