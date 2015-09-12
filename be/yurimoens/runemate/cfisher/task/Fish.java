package be.yurimoens.runemate.cfisher.task;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Fish extends Task {

    private final int[] FISHING_SPOT_MODELS = { -609731073 };
    private final Timer canFish;

    public Fish() {
        canFish = new Timer(Random.nextInt(2800, 4200));
    }

    @Override
    public boolean validate() {
        return (!Inventory.isFull()
                && Players.getLocal().getTarget() == null
                && !Players.getLocal().isMoving()
                && !canFish.isRunning()
                && !Npcs.newQuery().models(FISHING_SPOT_MODELS).results().isEmpty());
    }

    @Override
    public void execute() {
        Execution.delay(600, 1800);
        canFish.reset();
        canFish.start();

        Npc currentSpot = Npcs.newQuery().models(FISHING_SPOT_MODELS).results().nearest();

        if (!currentSpot.isVisible()) {
            Camera.turnTo(currentSpot);
        }

        //CMouse.fastInteract(currentSpot, "Use-rod");
        currentSpot.interact("Use-rod");
    }
}
