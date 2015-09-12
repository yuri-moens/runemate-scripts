package be.yurimoens.runemate.clividfarm.task;

import be.yurimoens.runemate.util.CExecution;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.task.Task;

public class ConvertLumber extends Task {

    private final int LUMBER = 20702;

    @Override
    public boolean validate() {
        return Inventory.contains(LUMBER) && GameObjects.newQuery().names("Diseased livid").results().isEmpty();
    }

    @Override
    public void execute() {
        CExecution.delayUntil(() -> {
            SpriteItem lumber = Inventory.getItems(LUMBER).last();
            if (lumber != null) {
                lumber.click();
            }

            return Inventory.getItems(LUMBER).isEmpty();
        }, Random.nextInt(1350, 1650), 9000, 10000);
    }
}
