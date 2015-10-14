package be.yurimoens.runemate.charper.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Play extends Task {

    private Timer timer = new Timer(0);

    @Override
    public boolean validate() {
        return Players.getLocal().getAnimationId() == -1 || !timer.isRunning();
    }

    @Override
    public void execute() {
        timer = new Timer(Random.nextInt(120000, 220000));
        timer.start();

        GameObject harp = GameObjects.newQuery().models(1276535684).results().nearest();

        CMouse.fastInteract(harp, "Play");

        Execution.delay(600, 1000);
    }
}
