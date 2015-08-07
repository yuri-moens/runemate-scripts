package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

import java.util.concurrent.Callable;

public class CExecution {

    public static boolean delayUntil(Callable<Boolean> callable, int frequency, int min, int max) {
        StopWatch timer;
        (timer = new StopWatch()).start();

        try {
            int random = (min == max) ? min : (int) Random.nextGaussian(min, max);
            boolean returnBoolean;

            while (!(returnBoolean = callable.call()) && timer.getRuntime() < random) {
                Execution.delay(frequency);
            }

            return returnBoolean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delayWhile(Callable<Boolean> callable, int frequency, int min, int max) {
        StopWatch timer;
        (timer = new StopWatch()).start();

        try {
            int random = (min == max) ? min : (int) Random.nextGaussian(min, max);
            boolean returnBoolean;

            while ((returnBoolean = callable.call()) && timer.getRuntime() < random) {
                Execution.delay(frequency);
            }

            return !returnBoolean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
