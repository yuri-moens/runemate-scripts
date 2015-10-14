package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Camera;
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

    enum Direction {
        NORTH,
        EAST,
        SOUTH,
        WEST;
    }

    public static boolean accurateInteract(GameObject gameObject, String action) {
        if (gameObject == null) {
            return false;
        }

        InteractablePoint centerPoint;
        InteractablePoint centerPoint2;

        try {
            do {
                centerPoint = gameObject.getModel().getBoundingRectangle().getCenterPoint();

                Execution.delay(150, 250);

                centerPoint2 = gameObject.getModel().getBoundingRectangle().getCenterPoint();
            } while (centerPoint.distance(centerPoint2) > 10D);
        } catch (NullPointerException e) {
            return false;
        }

        return fastInteract(gameObject, action);
    }

    public static boolean accurateInteract(Npc npc, String action) {
        if (npc == null) {
            return false;
        }

        InteractablePoint centerPoint;
        InteractablePoint centerPoint2;

        try {
            do {
                centerPoint = npc.getModel().getBoundingRectangle().getCenterPoint();

                Execution.delay(150, 250);

                centerPoint2 = npc.getModel().getBoundingRectangle().getCenterPoint();
            } while (centerPoint.distance(centerPoint2) > 10D);
        } catch (NullPointerException e) {
            return false;
        }

        return fastInteract(npc, action);
    }

    public static boolean fastInteract(Interactable interactable, String action) {
        if (interactable == null) {
            return false;
        }

        Mouse.move(interactable);

        Execution.delayUntil(() -> getTooltip().contains(action), 350, 550);

        if (getTooltip().contains(action)) {
            return interactable.click();
        } else {
            return interactable.interact(action);
        }
    }

    public static boolean interact(Interactable interactable, String action) {
        Mouse.click(interactable, Mouse.Button.RIGHT);

        if (Execution.delayUntil(Menu::isOpen, 1200, 1800)) {
            MenuItem menuItem;

            if ((menuItem = Menu.getItem(action)) != null) {
                return menuItem.click();
            } else {
                moveRandomFromPosition(200, 250, 200, 250);

                return false;
            }
        }

        return false;
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
        }

        if (Random.nextInt(2) == 1) {
            y *= -1;
        }

        concurrentlyMove(new InteractablePoint((int) Mouse.getPosition().getX() + x, (int) Mouse.getPosition().getY() + y));
    }

    public static void moveOffScreen() {
        if (Random.nextInt(0, 2) == 0) {
            moveOffScreen(Direction.EAST);
        } else {
            moveOffScreen(Direction.WEST);
        }
    }

    public static void moveOffScreen(Direction direction) {
        InteractablePoint point = new InteractablePoint(Mouse.getPosition());

        switch (direction) {
            case NORTH: point = new InteractablePoint(0, 0); break;
            case EAST: point = new InteractablePoint(0, 0); break;
            case SOUTH: point = new InteractablePoint(0, 0); break;
            case WEST: point = new InteractablePoint(0, 0); break;
        }

        concurrentlyMove(point);
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

    public static boolean hoverOption(Interactable interactable, String option) {
        Mouse.click(interactable, Mouse.Button.RIGHT);

        if (Execution.delayUntil(Menu::isOpen, 1200, 1800)) {
            MenuItem menuOption = Menu.getItem(option);

            if (menuOption != null) {
                Menu.getItem(option).hover();

                return true;
            } else {
                CMouse.moveRandomFromPosition(100, 250, 100, 250);
            }
        }

        return false;
    }

    public static boolean clickOption(LocatableEntity locatableEntity, String option) {
        return clickOption(locatableEntity, option, -1D);
    }

    public static boolean clickOption(LocatableEntity locatableEntity, String option, double angle) {
        if (Menu.isOpen()) {
            MenuItem menuOption = Menu.getItem(option);

            if (menuOption != null) {
                return menuOption.click();
            }
        }

        if (locatableEntity == null) {
            return false;
        }

        if (!locatableEntity.isVisible()) {
            if (angle < 0 || angle > 1) {
                Camera.turnTo(locatableEntity);
            } else {
                Camera.turnTo(locatableEntity, angle);
            }
        }

        return CExecution.delayUntil(() -> CMouse.fastInteract(locatableEntity, option), Random.nextInt(900, 1300), 5000, 6000);
    }

}
