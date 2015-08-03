package be.yurimoens.runemate.cabysscrafter.task.abyss;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class EnterRift extends Task {

    private final int RIFT_ID;

    public EnterRift(int riftId) {
        RIFT_ID = riftId;
    }

    @Override
    public boolean validate() {
        return (getParent().validate()
                && Constants.innerRing.contains(Players.getLocal())
                && !Inventory.containsAnyOf(Constants.DEGRADED_POUCH_IDS));
    }

    @Override
    public void execute() {
        GameObject rift = GameObjects.newQuery().filter((gameObject) -> gameObject.getId() == RIFT_ID).results().first();

        if (rift != null) {
            if (!rift.isVisible()) {
                Coordinate nearRift = rift.getPosition().randomize(1, 1);
                if (nearRift.isVisible()) {
                    nearRift.click();
                } else {
                    RegionPath path = RegionPath.buildTo(rift);
                    Execution.delayUntil(() -> {
                        path.step(true);
                        return rift.isVisible();
                    }, 8000, 10000);
                }
            }

            if (!rift.isVisible()) {
                Camera.turnTo(rift, 0.666D);
            }

            CMouse.hopClick(rift);
            Execution.delay(800, 1200);
            Execution.delayUntil(() -> !rift.isValid() || (!Players.getLocal().isMoving() && Players.getLocal().distanceTo(rift) <= 2D), 4000, 7000);
        }
    }
}
