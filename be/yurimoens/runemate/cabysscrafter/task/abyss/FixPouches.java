package be.yurimoens.runemate.cabysscrafter.task.abyss;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.util.CExecution;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
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
        Player player = Players.getLocal();
        Npc darkMage = Npcs.newQuery().filter(Npcs.getModelFilter(Constants.DARK_MAGE_MODEL)).results().first();

        if (!darkMage.isVisible()) {
            PredefinedPath.create(darkMage.getPosition().randomize(1, 1)).step(true);

            if (Execution.delayUntil(player::isMoving, 1200, 1800)) {
                Execution.delayUntil(() -> darkMage.isVisible() && player.distanceTo(darkMage) < 5D, 10000, 13000);
            }
        }

        CExecution.delayUntil(() -> darkMage.interact("Repair-pouches"), Random.nextInt(450, 650), 3500, 4000);

        Execution.delayWhile(() -> ChatDialog.getContinue() == null, 10000, 13000);

        if (ChatDialog.getContinue() != null) {
            Keyboard.type(" ", false);
        }
    }
}
