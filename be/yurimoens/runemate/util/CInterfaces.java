package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceContainer;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;

public class CInterfaces {

    public static boolean hasInterfaceContainer(int id) {
        for (InterfaceContainer c : Interfaces.getLoadedContainers())  {
            if (c.getIndex() == id) {
                return true;
            }
        }

        return false;
    }

}
