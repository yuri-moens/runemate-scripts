package be.yurimoens.runemate.cabysscrafter.task.abyss;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
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
        Player player = Players.getLocal();
        GameObject rift = GameObjects.newQuery().filter((gameObject) -> gameObject.getId() == RIFT_ID).results().first();

        if (rift != null) {
            if (!rift.isVisible() && player.distanceTo(rift) > 3D) {
                Coordinate nearRift = rift.getPosition().randomize(2, 2);

                while (!nearRift.isValid() || !nearRift.isReachable()) {
                    nearRift = rift.getPosition().randomize(2, 2);
                }

                if (nearRift.isVisible()) {
                    nearRift.click();
                } else {
                    RegionPath.buildTo(nearRift).step(true);
                }

                if (Execution.delayUntil(player::isMoving, 1500, 2500)) {
                    if (Execution.delayWhile(player::isMoving, 9000, 11000)) {
                        Execution.delay(Random.nextInt(700, 1000));
                    }
                }
            }

            if (!rift.isVisible() && !Players.getLocal().isMoving()) {
                Camera.turnTo(rift);
            }

            if (CExecution.delayUntil(() -> CMouse.fastInteract(rift, "Exit-through"), Random.nextInt(450, 650), 3600, 4000)) {
                Execution.delay(800, 1200);
                Execution.delayUntil(() -> !rift.isValid() || (player.isMoving() && player.distanceTo(rift) <= 2D), 4000, 7000);
            }
        }
    }
}
