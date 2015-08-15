package be.yurimoens.runemate.cabysscrafter.task.walking;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.Worlds;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.navigation.Traversal;
import com.runemate.game.api.hybrid.location.navigation.web.WebPath;
import com.runemate.game.api.hybrid.queries.results.WorldQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.awt.event.KeyEvent;

class WalkBack extends Task {

    private final SlotAction freedom;

    public WalkBack() {
        freedom = ActionBar.getFirstAction("Freedom");
    }

    @Override
    public boolean validate() {
        WalkToAbyss parent = (WalkToAbyss) getParent();
        return (parent.validate()
                && parent.underAttack
                && Players.getLocal().getPosition().getY() > Constants.wildernessWall.getY());
    }

    @Override
    public void execute() {
        WebPath path = Traversal.getDefaultWeb().getPathBuilder().buildTo(Constants.wildernessWall);

        if (Execution.delayUntil(() -> {
            if (freedom != null && !freedom.isCoolingDown()) {
                freedom.activate(false);
                Execution.delayUntil(freedom::isCoolingDown, 1000);
            }

            GameObject wall = GameObjects.newQuery().models(Constants.WILDERNESS_WALLS)
                    .filter((gameObject) -> gameObject.getPosition().getX() == 3102 || gameObject.getPosition().getX() == 3103 || gameObject.getPosition().getX() == 3104)
                    .results().nearest();

            if (wall != null && wall.isVisible() && Players.getLocal().getPosition().getY() > Constants.wildernessWall.getY()) {
                CMouse.fastInteract(wall, "Cross");
                Player player = Players.getLocal();
                Execution.delayWhile(player::isMoving);
                Execution.delayUntil(() -> player.getPosition().getY() <= Constants.wildernessWall.getY(), 2500, 3500);
            } else if (path != null) {
                path.step(true);
                Execution.delayUntil(() -> Players.getLocal().isMoving(), 1200, 1800);
            }

            return Players.getLocal().getPosition().getY() <= Constants.wildernessWall.getY();
        }, 10000, 13000)) {
            hopWorld();

            ((WalkToAbyss) getParent()).underAttack = false;
        }
    }

    private void hopWorld() {
        int currentWorld = Worlds.getCurrent();
        WorldQueryResults worlds = Worlds.newQuery().member().filter((worldOverview) -> worldOverview.getId() != currentWorld).results();

        while (Players.getLocal().getStanceId() == 18012) {
            Execution.delay(1000, 1500);
        }

        InterfaceComponent hopWorldsComponent = null;

        while (hopWorldsComponent == null || !hopWorldsComponent.isVisible()) {
            Keyboard.typeKey(KeyEvent.VK_ESCAPE);
            Execution.delayUntil(() -> Interfaces.getAt(1433, 148) != null, 4000, 5000);
            hopWorldsComponent = Interfaces.getAt(1433, 148);
        }

        InterfaceComponent viewPortComponent = Interfaces.getAt(1587, 25);

        while (viewPortComponent == null || !viewPortComponent.isVisible()) {
            hopWorldsComponent.click();
            Execution.delayUntil(() -> Interfaces.getAt(1587, 25) != null, 4000, 5000);
            viewPortComponent = Interfaces.getAt(1587, 25);
        }

        InterfaceComponent worldComponent = Interfaces.newQuery().texts("" + worlds.random().getId()).results().random();

        Mouse.move(viewPortComponent);

        while (!viewPortComponent.getBounds().contains(worldComponent.getBounds())) {
            Mouse.scroll(true, Random.nextInt(100, 200));
        }

        InterfaceComponent yesButtonComponent = Interfaces.getAt(1587, 87);

        while (yesButtonComponent == null || !yesButtonComponent.isVisible()) {
            worldComponent.click();
            Execution.delayUntil(() -> Interfaces.getAt(1587, 87) != null, 4000, 5000);
            yesButtonComponent = Interfaces.getAt(1587, 87);
        }

        yesButtonComponent.click();

        Execution.delay(5000, 7500);

        Execution.delayUntil(() -> Players.getLocal().getPosition() != null && Players.getLocal().getPosition().distanceTo(Constants.wildernessWall) < 10D, 5000, 7500);
        Camera.turnTo(320, 0.666D);
    }

}
