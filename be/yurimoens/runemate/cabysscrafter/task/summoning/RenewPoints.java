package be.yurimoens.runemate.cabysscrafter.task.summoning;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.Location;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.rs3.local.hud.interfaces.Summoning;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.ActionBar;
import com.runemate.game.api.rs3.local.hud.interfaces.eoc.SlotAction;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class RenewPoints extends Task {

    SlotAction tokkulZoSlot;

    public RenewPoints() {
        tokkulZoSlot = ActionBar.getFirstAction("TokKul-Zo (Charged)");
    }

    @Override
    public boolean validate() {
        return Location.getLocation() == Location.TZHAAR_PLAZA;
    }

    @Override
    public void execute() {
        GameObject obelisk = GameObjects.newQuery().names("Small obelisk").results().nearest();

        if (!obelisk.isVisible()) {
            PredefinedPath.create(new Coordinate(4661, 5140, 0)).step(true);
            Player player = Players.getLocal();

            if (Execution.delayUntil(player::isMoving, 3000, 4000)) {
                Execution.delayWhile(player::isMoving);
            }
        }

        if (!obelisk.isVisible()) {
            Camera.turnTo(obelisk);
        }

        Execution.delayWhile(() -> {
            CMouse.fastInteract(obelisk, "Renew points");

            Execution.delayWhile(() -> Summoning.getPoints() <= 10, 3000, 4000);

            return Summoning.getPoints() <= 10;
        });

        CExecution.delayWhile(() -> {
            tokkulZoSlot.activate(false);

            return Interfaces.getAt(Constants.TELEPORT_INTERFACE, 0) == null;
        }, Random.nextInt(520, 870), 3000, 4000);

        if (Interfaces.getAt(Constants.TELEPORT_INTERFACE, 0) != null) {
            Keyboard.type("4", false);
        }

        Execution.delayUntil(() -> Location.getLocation() == Location.EDGEVILLE || Location.getLocation() == Location.TZHAAR_CAVES, 7000, 9000);
    }
}
