package be.yurimoens.runemate.cthiever.task;

import be.yurimoens.runemate.util.CExecution;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Lure extends Task {

    private Npc volunteer;

    @Override
    public boolean validate() {
        volunteer = Npcs.newQuery().filter((Npc npc) -> {
            if (npc.getDefinition() == null || npc.getDefinition().getLocalState() == null) {
                return false;
            }

            return npc.getDefinition().getLocalState().getName().equals("Coshing volunteer");
        }).results().nearest();
        return volunteer != null && volunteer.getAnimationId() == -1 && !Players.getLocal().getSpotAnimationIds().contains(4531);
    }

    @Override
    public void execute() {
        volunteer.interact("Lure");

        if (Execution.delayUntil(() -> ChatDialog.getContinue() != null, 1200, 1600)) {
            Keyboard.type(" ", false);

            Execution.delay(800, 1300);

            if (Execution.delayUntil(() -> ChatDialog.getContinue() != null, 1200, 1600)) {
                Keyboard.type(" ", false);
            } else {
                return;
            }
        } else {
            return;
        }

        CExecution.delayUntil(() -> volunteer.interact("Knock-out"), Random.nextInt(700, 1000), 3000, 4000);

        Execution.delayUntil(() -> volunteer.getAnimationId() == 12413, 3000, 4000);

        while (volunteer.getAnimationId()  == 12413) {
            volunteer.interact("Loot");
            Execution.delay(580, 735);
        }
    }
}
