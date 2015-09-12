package be.yurimoens.runemate.clividfarm.task;

import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class TakeProduce extends Task {

    private final int PRODUCE = 20704;
    private final int BUNCH = 20705;

    private GameObject producePile;

    @Override
    public boolean validate() {
        return ((producePile = GameObjects.newQuery().names("Produce pile (full)").results().first()) != null
                        || Inventory.contains(PRODUCE)) && GameObjects.newQuery().names("Diseased livid").results().isEmpty();
    }

    @Override
    public void execute() {
        if (producePile != null && !producePile.isVisible()) {
            Camera.turnTo(producePile, 0.666D);
        }

        if (!Inventory.contains(PRODUCE)) {
            CMouse.fastInteract(producePile, "Take");

            if (!Execution.delayUntil(() -> Inventory.contains(PRODUCE), 3000, 4000)) {
                return;
            }
        }

        CExecution.delayUntil(() -> {
            SpriteItem produce = Inventory.getItems(PRODUCE).last();
            if (produce != null) {
                produce.click();
            }

            return Inventory.getItems(BUNCH).size() == 2;
        }, Random.nextInt(1100, 1500), 7000, 8000);
    }
}
