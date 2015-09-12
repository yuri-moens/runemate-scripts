package be.yurimoens.runemate.clividfarm.task;

import be.yurimoens.runemate.util.CExecution;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class EncouragePauline extends Task {

    private Npc pauline;
    private Timer timeout;

    @Override
    public boolean validate() {
        return (timeout == null || !timeout.isRunning()) && (pauline = Npcs.newQuery().names("Drained Pauline Polaris").results().first()) != null;
    }

    @Override
    public void execute() {
        if (!pauline.isVisible()) {
            Camera.turnTo(pauline, 0.666D);
        }

        pauline.interact("Encourage");

        if (!Execution.delayUntil(() -> !ChatDialog.getOptions().isEmpty(), 2000, 3000)) {
            if (ChatDialog.getContinue() != null) {
                Keyboard.type(" ", false);
            }

            return;
        }

        timeout = new Timer(Random.nextInt(4000, 5000));
        timeout.start();

        Keyboard.type("" + ChatDialog.getOption(
                "Lokar will really appreciate this.",
                "Look at all the produce being made.",
                "Come on, you're doing so well.",
                "You're doing a fantastic job.",
                "Keep going! We can do this.",
                "Extraordinary!"
        ).getNumber(), false);
    }
}
