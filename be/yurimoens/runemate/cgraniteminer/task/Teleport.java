package be.yurimoens.runemate.cgraniteminer.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class Teleport extends Task {

    @Override
    public boolean validate() {
        return (Inventory.isFull() && GameObjects.newQuery().names("Granite rocks").results().isEmpty() && Players.getLocal().getAnimationId() == -1);
    }

    @Override
    public void execute() {

    }
}
