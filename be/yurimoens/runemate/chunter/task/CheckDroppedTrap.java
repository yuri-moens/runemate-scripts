package be.yurimoens.runemate.chunter.task;

import com.runemate.game.api.hybrid.entities.GroundItem;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GroundItems;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

/**
 * Created by ymo on 9/24/15.
 */
public class CheckDroppedTrap extends Task {

    private LocatableEntityQueryResults<GroundItem> traps;

    @Override
    public boolean validate() {
        return !(traps = GroundItems.newQuery().names("Box trap").results()).isEmpty();
    }

    @Override
    public void execute() {
        traps.nearest().interact("Take");

        Execution.delayUntil(() -> GroundItems.newQuery().names("Box trap").results().isEmpty(), 1800, 2400);
    }
}
