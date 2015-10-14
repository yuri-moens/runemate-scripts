package be.yurimoens.runemate.ccutter.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Idle extends Task {

    private Timer countdown;

    public Idle() {
        countdown = new Timer(Random.nextInt(100000, 210000));

        countdown.start();
    }

    @Override
    public boolean validate() {
        return !countdown.isRunning();
    }

    @Override
    public void execute() {
        countdown = new Timer(Random.nextInt(100000, 210000));
        countdown.start();

        if (Random.nextInt(0, 13) == 0) {
            Camera.turnTo(Camera.getYaw() + Random.nextInt(-30, 30));
            int yaw = Camera.getYaw();
            double pitch = Camera.getPitch();

            for (int i = 0; i < Random.nextInt(2, 5); i++) {
                Camera.turnTo(Camera.getYaw() + Random.nextInt(-30, 31));
            }

            Camera.turnTo(yaw, pitch);
        } else {
            Mouse.click(GameObjects.getLoaded().random(), Mouse.Button.RIGHT);

            Execution.delay(200, 600);

            CMouse.moveRandomFromPosition(-150, 150, -150, 150);
        }
    }
}
