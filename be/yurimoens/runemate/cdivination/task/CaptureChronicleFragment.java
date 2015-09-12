package be.yurimoens.runemate.cdivination.task;

import be.yurimoens.runemate.cdivination.Constants;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class CaptureChronicleFragment extends Task {

    private LocatableEntityQueryResults<Npc> chronicleFragments;

    @Override
    public boolean validate() {
        chronicleFragments = Npcs.newQuery()
                .filter((npc) -> npc.getDefinition().getName().startsWith("Chronicle")).results();

        return ((!Inventory.isFull() || Inventory.contains(Constants.CHRONICLE_FRAGMENT))
                && !chronicleFragments.isEmpty());
    }

    @Override
    public void execute() {
        int amountOfFragments = Inventory.getQuantity(Constants.CHRONICLE_FRAGMENT);

        Npc chronicleFragment = chronicleFragments.nearest();

        Camera.turnTo(chronicleFragment);

        Timer timeout = new Timer(Random.nextInt(3000, 4500));
        timeout.start();


        chronicleFragment.getPosition().click();

        Execution.delay(1800, 2400);

        Execution.delayUntil(() -> Players.getLocal().isMoving()
                || Inventory.getQuantity(Constants.CHRONICLE_FRAGMENT) > amountOfFragments, 1200, 1800);
    }
}
