package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.Location;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.framework.task.Task;

public class WalkToAbyss extends Task {

    public boolean underAttack;

    public WalkToAbyss() {
        add(new WalkToWilderness(), new WalkToMage(), new TeleportToAbyss(), new WalkBack());
    }

    @Override
    public boolean validate() {
        Location location = Location.getLocation();
        return (location == Location.EDGEVILLE || location == Location.WILDERNESS || location == Location.MAGE_AREA)
                && Inventory.contains(Constants.PURE_ESSENCE)
                && Inventory.isFull();
    }


    @Override
    public void execute() {}
}
