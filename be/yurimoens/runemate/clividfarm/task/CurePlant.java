package be.yurimoens.runemate.clividfarm.task;

import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.HashMap;

public class CurePlant extends Task {

    private final HashMap<Coordinate, Integer> positionsMap;
    private final Coordinate startPoint = new Coordinate(2107, 3946, 0);

    private LocatableEntityQueryResults<GameObject> diseasedPlants;

    public CurePlant() {
        positionsMap =  new HashMap<>();
        Coordinate topLeft = new Coordinate(2098, 3949, 0);

        positionsMap.put(topLeft, 1);
        positionsMap.put(new Coordinate(topLeft.getX() + 2, topLeft.getY(), topLeft.getPlane()), 1);
        positionsMap.put(new Coordinate(topLeft.getX() + 4, topLeft.getY(), topLeft.getPlane()), 1);
        positionsMap.put(new Coordinate(topLeft.getX() + 6, topLeft.getY(), topLeft.getPlane()), 2);
        positionsMap.put(new Coordinate(topLeft.getX() + 8, topLeft.getY(), topLeft.getPlane()), 1);
        positionsMap.put(new Coordinate(topLeft.getX(), topLeft.getY() - 3, topLeft.getPlane()), 4);
        positionsMap.put(new Coordinate(topLeft.getX() + 2, topLeft.getY() - 3, topLeft.getPlane()), 3);
        positionsMap.put(new Coordinate(topLeft.getX() + 4, topLeft.getY() - 3, topLeft.getPlane()), 2);
        positionsMap.put(new Coordinate(topLeft.getX() + 6, topLeft.getY() - 3, topLeft.getPlane()), 2);
        positionsMap.put(new Coordinate(topLeft.getX() + 8, topLeft.getY() - 3, topLeft.getPlane()), 2);
        positionsMap.put(new Coordinate(topLeft.getX(), topLeft.getY() - 6, topLeft.getPlane()), 3);
        positionsMap.put(new Coordinate(topLeft.getX() + 2, topLeft.getY() - 6, topLeft.getPlane()), 4);
        positionsMap.put(new Coordinate(topLeft.getX() + 4, topLeft.getY() - 6, topLeft.getPlane()), 4);
        positionsMap.put(new Coordinate(topLeft.getX() + 6, topLeft.getY() - 6, topLeft.getPlane()), 4);
        positionsMap.put(new Coordinate(topLeft.getX() + 8, topLeft.getY() - 6, topLeft.getPlane()), 3);
    }

    @Override
    public boolean validate() {
        return !(diseasedPlants = GameObjects.newQuery().names("Diseased livid").results()).isEmpty();
    }

    @Override
    public void execute() {
        GameObject diseasedPlant = diseasedPlants.nearest();

        Player player = Players.getLocal();
        if (player.distanceTo(startPoint) > 2D) {

            if (startPoint.isVisible()) {
                startPoint.click();
            } else {
                PredefinedPath path = PredefinedPath.create(new Coordinate(startPoint.getX(), startPoint.getY() - 1, startPoint.getPlane()));

                if (path != null) {
                    path.step(true);
                }
            }

            if (Execution.delayUntil(player::isMoving, 1200, 1800)) {
                Execution.delayWhile(player::isMoving);
                Execution.delay(800, 1200);
            }
        }

        if (!diseasedPlant.isVisible()) {
            Camera.turnTo(diseasedPlant, 0.666D);
        }

        CMouse.fastInteract(diseasedPlant, "Cure-plant");

        if (!Execution.delayUntil(() -> Interfaces.getAt(1081, 9) != null, 2000, 3000)) {
            return;
        }

        Execution.delay(400, 600);

        Keyboard.type("" + positionsMap.get(diseasedPlant.getPosition()), false);

        Execution.delay(1400, 1900);
    }
}
