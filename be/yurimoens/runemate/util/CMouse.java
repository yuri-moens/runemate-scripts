package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.InteractablePoint;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;

public class CMouse {

    public static boolean fastInteract(Interactable interactable, String action) {
        if (interactable == null) {
            return false;
        }
        Mouse.move(interactable);
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

    public static void moveRandomFromPosition() {
        moveRandomFromPosition(40, 40);
    }

    public static void moveRandomFromPosition(int xRandom, int yRandom) {
        Mouse.move(new InteractablePoint((int) Mouse.getPosition().getX() + Random.nextInt(-xRandom, xRandom),(int) Mouse.getPosition().getY() + Random.nextInt(-yRandom, yRandom)));
    }

    public static void moveToMinimap() {
        Mouse.move(Interfaces.getAt(1465, 0).getInteractionPoint());
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
