package be.yurimoens.runemate.chunter.task;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class DismantleTrap extends Task {

    private LocatableEntityQueryResults<GameObject> traps;

    @Override
    public boolean validate() {
        return !(traps = GameObjects.newQuery().filter((GameObject gameObject) -> gameObject.getId() == 91231 || gameObject.getId() == 19192).results()).isEmpty();
    }

    @Override
    public void execute() {
        int trapCount = Inventory.getQuantity("Box trap");

        String action = null;

        for (String trapAction : traps.nearest().getDefinition().getActions()) {
            if (trapAction.equals("Dismantle")) {
                action = "Dismantle";
                break;
            } else if (trapAction.equals("Check")) {
                action = "Check";
                break;
            }
        }

        if (!traps.nearest().interact(action)) {
            return;
        }

        Execution.delayUntil(() -> Inventory.getQuantity("Box trap") > trapCount, 3000, 4000);
    }
}
