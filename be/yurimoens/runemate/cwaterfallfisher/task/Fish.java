package be.yurimoens.runemate.cwaterfallfisher.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.task.Task;

public class Fish extends Task {

    private final int FISHING_SPOT_MODEL;

    private Timer canFish;
    private Npc fishSpot;

    public Fish() {
        canFish = new Timer(0);

        int fishingLevel = Skill.FISHING.getBaseLevel();

        if (fishingLevel >= 97) {
            FISHING_SPOT_MODEL = -126628624;
        } else if (fishingLevel >= 95) {
            FISHING_SPOT_MODEL = -558772543;
        } else {
            FISHING_SPOT_MODEL = -1289791102;
        }
    }

    @Override
    public boolean validate() {
        Player player = Players.getLocal();

        return player.getTarget() == null &&
                !player.isMoving() &&
                !canFish.isRunning() &&
                (fishSpot = Npcs.newQuery().models(FISHING_SPOT_MODEL).results().nearest()) != null;
    }

    @Override
    public void execute() {
        canFish = new Timer(Random.nextInt(2800, 4200));
        canFish.start();

        if (!fishSpot.isVisible()) {
            Camera.turnTo(fishSpot, 0.666D);
        }

        CMouse.fastInteract(fishSpot, "Lure");
    }
}
