package be.yurimoens.runemate.cabysscrafter.task.abyss;

import be.yurimoens.runemate.cabysscrafter.Constants;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

class FixPouches extends Task {

    @Override
    public boolean validate() {
        return (getParent().validate()
                && Constants.innerRing.contains(Players.getLocal())
                && Inventory.containsAnyOf(Constants.DEGRADED_POUCH_IDS));
    }

    @Override
    public void execute() {
        Npc darkMage = Npcs.newQuery().filter(Npcs.getModelFilter(Constants.DARK_MAGE_MODEL)).results().first();

        if (!darkMage.isVisible()) {
            PredefinedPath path = PredefinedPath.create(darkMage.getPosition().randomize(1, 1));

            Execution.delayUntil(() -> {
                path.step(true);
                return darkMage.isVisible();
            }, 10000, 13000);
        }

        darkMage.interact("Repair-pouches");

        if (Players.getLocal().distanceTo(darkMage) > 2D) {
            Execution.delay(900, 1600);
        }

        Execution.delayWhile(() -> ChatDialog.getContinue() == null && Players.getLocal().isMoving(), 10000, 13000);

        if (ChatDialog.getContinue() != null) {
            Keyboard.type(" ", false);
        }
    }
}
