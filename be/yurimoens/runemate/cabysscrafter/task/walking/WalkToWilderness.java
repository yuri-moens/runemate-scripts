package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class WalkToWilderness extends Task {

    private GameObject wall;

    @Override
    public boolean validate() {
        Player player = Players.getLocal();

        return (getParent().validate()
                && player.getPosition().getY() < Constants.wildernessWall.getY()
                && player.getAnimationId() == -1
                && (wall = GameObjects.newQuery().models(Constants.WILDERNESS_WALLS)
                    .filter((gameObject) -> gameObject.getPosition().getX() == 3102 || gameObject.getPosition().getX() == 3103 || gameObject.getPosition().getX() == 3104)
                    .results().nearest()) != null);
    }

    @Override
    public void execute() {
        Player player = Players.getLocal();

        if (!wall.isVisible()) {
            Coordinate nearWall = new Coordinate(Constants.wildernessWall.getX(), Constants.wildernessWall.getY() - 2);

            RegionPath.buildTo(nearWall).step(true);

            Camera.concurrentlyTurnTo(350, 0.300D, 0.10D);

            if (Execution.delayUntil(player::isMoving, 1200, 1800)) {
                Execution.delayUntil(wall::isVisible, 13000, 16000);
                Execution.delay(0, 1800);
            }
        }

        if (wall.isVisible()) {
            CExecution.delayUntil(() -> CMouse.fastInteract(wall, "Cross"), Random.nextInt(450, 650), 4000, 5000);

            Execution.delayUntil(player::isMoving, 1200, 1800);

            CMouse.moveToMinimap();

            Execution.delayUntil(() -> Players.getLocal().getPosition().getY() >= Constants.wildernessWall.getY() || Players.getLocal().getAnimationId() != -1 || ChatDialog.getContinue() != null, 9000, 11000);
        }
    }
}
