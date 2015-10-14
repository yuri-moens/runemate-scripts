package be.yurimoens.runemate.cthiever.task;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Pickpocket extends Task {

    @Override
    public boolean validate() {
        Player player = Players.getLocal();
        return !player.getSpotAnimationIds().contains(4531) && Health.getCurrentPercent() > Random.nextInt(3, 6) && !player.isMoving();
    }

    @Override
    public void execute() {
        Npc farmer = Npcs.newQuery().names("Master Farmer").results().nearest();

        if (!farmer.isVisible()) {
            Camera.turnTo(farmer);
        }

        farmer.click();

        Execution.delay(0, 500);
    }
}
