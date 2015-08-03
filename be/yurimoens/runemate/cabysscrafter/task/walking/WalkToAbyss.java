package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class WalkToAbyss extends Task {

    public WalkToAbyss() {
        add(new WalkToWildernessWall(), new JumpWildernessWall(), new WalkToMage(), new TeleportToAbyss());
    }

    @Override
    public boolean validate() {
        return (Players.getLocal().distanceTo(Constants.wildernessWall) < 50D
                && Inventory.contains(Constants.PURE_ESSENCE)
                && Inventory.isFull());
    }


    @Override
    public void execute() {}
}
