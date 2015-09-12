package be.yurimoens.runemate.cdivination.task;

import com.runemate.game.api.hybrid.entities.Actor;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.location.navigation.basic.BresenhamPath;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class RunFromCombat extends Task {

    @Override
    public boolean validate() {
        LocatableEntityQueryResults<Npc> mobs = Npcs.newQuery().filter((npc) -> npc.getDefinition().getLevel() > 0).results();

        for (Npc mob : mobs) {
            Actor target;
            if ((target = mob.getTarget()) != null && target.getName().equals(Players.getLocal().getName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void execute() {
        GameObject rift = GameObjects.newQuery().names("Energy rift").results().first();

        BresenhamPath.buildTo(rift).step(true);

        Execution.delay(1200, 1800);
        Execution.delayWhile(() -> Players.getLocal().isMoving(), 6000, 7000);
    }
}
