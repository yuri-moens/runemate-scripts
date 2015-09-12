package be.yurimoens.runemate.cgranitecutter.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceContainer;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.framework.task.Task;

public class CutGranite extends Task {

    private boolean isAnimating() {
        Timer timeout = new Timer(1000L);
        timeout.start();

        while (timeout.getRemainingTime() != 0) {
            if (Players.getLocal().getAnimationId() != -1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean validate() {
        return (!Inventory.isFull() && !isAnimating());
    }

    @Override
    public void execute() {
        //ActionBar.getFirstAction(6983).activate();
        Keyboard.type("1", false);
        Timer timeout = new Timer(2000L);
        timeout.start();
        boolean interfaceFound = false;
        while (timeout.getRemainingTime() != 0 && !interfaceFound) {
            for (InterfaceContainer c : Interfaces.getLoadedContainers())  {
                if (c.getIndex() == 1371) {
                    interfaceFound = true;
                    break;
                }
            }
        }

        if (interfaceFound) {
            Keyboard.type(" ", false);
        }
    }
}
