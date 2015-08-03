package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class JumpWildernessWall extends Task {

    @Override
    public boolean validate() {
        return (getParent().validate()
                && Players.getLocal().getPosition().getY() < Constants.wildernessWall.getY()
                && Players.getLocal().distanceTo(Constants.wildernessWall) < 8D
                && Players.getLocal().getAnimationId() == -1);
    }

    @Override
    public void execute() {
        CMouse.hopClick(
                GameObjects.newQuery().models(Constants.WILDERNESS_WALLS)
                        .filter((gameObject) -> gameObject.getPosition().getX() == 3102 || gameObject.getPosition().getX() == 3103 || gameObject.getPosition().getX() == 3104)
                        .results().nearest()
        );

        CMouse.moveToMinimap();

        Execution.delayUntil(() -> Players.getLocal().getPosition().getY() >= Constants.wildernessWall.getY() || Players.getLocal().getAnimationId() != -1, 4500, 6000);
    }
}
