package be.yurimoens.runemate.cabysscrafter.task.abyss;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.Location;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
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
                && Location.getLocation() == Location.INNER_RING
                && !Inventory.containsAnyOf(Constants.DEGRADED_POUCH_IDS));
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();
        GameObject rift = GameObjects.newQuery().filter((gameObject) -> gameObject.getId() == RIFT_ID).results().first();

        if (rift != null) {
            if (!rift.isVisible() && player.distanceTo(rift) > 3D) {
                Coordinate nearRift = getCoordinateNearRift(rift);

                if (nearRift.isVisible()) {
                    nearRift.click();
                } else {
                    PredefinedPath.create(nearRift).step(true);
                }

                if (Execution.delayUntil(player::isMoving, 1500, 2500)) {
                    if (Execution.delayWhile(player::isMoving, 9000, 11000)) {
                        Execution.delay(Random.nextInt(900, 1200));
                    }
                }
            }

            if (!rift.isVisible() && !Players.getLocal().isMoving()) {
                Camera.turnTo(rift);
            }

            if (CExecution.delayUntil(() -> CMouse.accurateInteract(rift, "Exit-through"), Random.nextInt(800, 1100), 3600, 4000)) {
                Execution.delay(800, 1200);
                Execution.delayUntil(() -> !rift.isValid() || (player.isMoving() && player.distanceTo(rift) <= 2D), 4000, 7000);
            }
        }
    }

    private Coordinate getCoordinateNearRift(GameObject rift) {
        Coordinate nearRift = null;

        final int riftX = rift.getPosition().getX();
        final int riftY = rift.getPosition().getY();
        final int riftPlane = rift.getPosition().getPlane();

        switch (rift.getOrientationAsAngle()) {
            case 0: nearRift = new Coordinate(riftX - 1, riftY + (int)Random.nextGaussian(-1, 2), riftPlane); break;
            case 90: nearRift = new Coordinate(riftX + (int)Random.nextGaussian(-1, 2), riftY + 1, riftPlane); break;
            case 180: nearRift = new Coordinate(riftX + 1, riftY + (int)Random.nextGaussian(-1, 2), riftPlane); break;
            case 270: nearRift = new Coordinate(riftX + (int)Random.nextGaussian(-1, 2), riftY - 1, riftPlane); break;
        }

        return nearRift;
    }
}
