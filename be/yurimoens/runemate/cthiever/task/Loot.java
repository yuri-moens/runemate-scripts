package be.yurimoens.runemate.cthiever.task;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class Loot extends Task {

    private Npc volunteer;

    @Override
    public boolean validate() {
        volunteer = Npcs.newQuery().filter((Npc npc) -> {
            if (npc.getDefinition() == null || npc.getDefinition().getLocalState() == null) {
                return false;
            }

            return npc.getDefinition().getLocalState().getName().equals("Coshing volunteer");
        }).results().nearest();
        return volunteer != null && volunteer.getAnimationId() == 12413 && !Players.getLocal().getSpotAnimationIds().contains(4531);
    }

    @Override
    public void execute() {
        volunteer.interact("Loot");
    }
}
