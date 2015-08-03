package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.InteractablePoint;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

import java.awt.*;

public class CMouse {

    public static void hop(Interactable interactable) {
        Mouse.PathGenerator pathGenerator = Mouse.getPathGenerator();

        Mouse.setPathGenerator(new Mouse.PathGenerator() {
            @Override
            public boolean move(Interactable interactable, Filter<Point> filter, double v) {
                InteractablePoint interactablePoint;
                return (interactablePoint = interactable.getInteractionPoint()) != null && hop(interactablePoint);
            }
        });

        Mouse.move(interactable);

        Mouse.setPathGenerator(pathGenerator);
    }

    public static boolean hopClick(Interactable interactable) {
        hop(interactable);
        return Mouse.click(Mouse.Button.LEFT);
    }

    public static boolean fastInteract(Interactable interactable, String action) {
        Mouse.move(interactable);
        return interact(interactable, action);
    }

    public static boolean hopInteract(Interactable interactable, String action) {
        hop(interactable);
        return interact(interactable, action);
    }

    public static String getTooltip() {
        InterfaceComponent tooltip = Interfaces.getAt(1477, 805, 0);
        if (tooltip == null || !tooltip.isValid()) {
            return "";
        } else {
            return tooltip.getText();
        }
    }

    public static void moveToMinimap() {
        Mouse.move(Interfaces.getAt(1465, 0).getInteractionPoint());
    }

    private static boolean interact(Interactable interactable, String action) {
        Timer timeout = new Timer(Random.nextInt(500, 1200));
        timeout.start();
        while (timeout.getRemainingTime() != 0 && !getTooltip().contains(action)) {
            Execution.delay(150, 250);
        }

        if (getTooltip().contains(action)) {
            return Mouse.click(Mouse.Button.LEFT);
        } else {
            return interactable.interact(action);
        }
    }

}
