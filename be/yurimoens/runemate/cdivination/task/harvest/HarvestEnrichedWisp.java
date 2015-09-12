package be.yurimoens.runemate.cdivination.task.harvest;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class HarvestEnrichedWisp extends Task {

    private LocatableEntityQueryResults<Npc> enrichedWisps;

    @Override
    public boolean validate() {
        enrichedWisps = Npcs.newQuery().filter(
                (npc) -> npc.getDefinition().getName().startsWith("Enriched")).results();

        return (!enrichedWisps.isEmpty() && Players.getLocal().distanceTo(enrichedWisps.nearest()) >= 2.5D);
    }

    @Override
    public void execute() {
        Npc nearest = enrichedWisps.nearest();

        if (!nearest.isVisible()) {
            Camera.turnTo(nearest);
        }

        CMouse.fastInteract(enrichedWisps.nearest(), "Harvest");

        Execution.delayUntil(() -> Players.getLocal().isMoving()
                || Players.getLocal().distanceTo(nearest) < 2.5D, 1200, 1800);
    }
}
