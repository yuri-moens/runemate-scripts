package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.InteractablePoint;
import com.runemate.game.api.hybrid.local.hud.Menu;
import com.runemate.game.api.hybrid.local.hud.MenuItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CMouse {

    public static boolean fastInteract(Interactable interactable, String action) {
        if (interactable == null) {
            return false;
        }
        Mouse.move(interactable);
        return interact(interactable, action);
    }

    public static String getTooltip() {
        List<MenuItem> menuItems;

        return (menuItems = Menu.getItems()).isEmpty() ? "" : menuItems.get(0).getAction();
    }

    public static void moveRandomFromPosition() {
        moveRandomFromPosition(40, 40);
    }

    public static void moveRandomFromPosition(int x, int y) {
        moveRandomFromPosition(0, x, 0, y);
    }

    public static void moveRandomFromPosition(int xLower, int xUpper, int yLower, int yUpper) {
        int x = (int) Random.nextGaussian((double) xLower, (double) xUpper);
        int y = (int) Random.nextGaussian((double) yLower, (double) yUpper);

        if (Random.nextInt(2) == 1) {
            x *= -1;
            y *= -1;
        }

        concurrentlyMove(new InteractablePoint((int) Mouse.getPosition().getX() + x, (int) Mouse.getPosition().getY() + y));
    }

    public static Future<Boolean> concurrentlyMove(Interactable target) {
        if (target == null || target.getInteractionPoint() == null) {
            return new FutureTask<>(() -> false);
        }

        FutureTask task = new FutureTask(() -> Mouse.move(target));

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(task);

        return task;
    }

    public static void moveToMinimap() {
        concurrentlyMove(Interfaces.getAt(1465, 0).getInteractionPoint());
    }

    private static boolean interact(Interactable interactable, String action) {
        Execution.delayUntil(() -> getTooltip().contains(action), 350, 550);

        if (getTooltip().contains(action)) {
            return interactable.click();
        } else {
            return interactable.interact(action);
        }
    }

}
