package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;

public class CBank {

    public static boolean open() {
        return open(Banks.getLoaded().nearest(), "Bank");
    }

    public static boolean open(LocatableEntity bank, String action) {
        Player player;
        return Bank.isOpen() || (bank != null && bank.isVisible() && CMouse.fastInteract(bank, action) && (player = Players.getLocal()) != null && Execution.delayUntil(Bank::isOpen, player::isMoving, 1500, 2500));
    }

}
