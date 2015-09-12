package be.yurimoens.runemate.cslayeraid.task;

import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.region.Projectiles;
import com.runemate.game.api.osrs.local.hud.interfaces.Prayer;
import com.runemate.game.api.script.framework.task.Task;

public class TurnPrayerOff extends Task {

    @Override
    public boolean validate() {
        return (Projectiles.newQuery().target(Players.getLocal()).results().isEmpty() && !Prayer.getActivePrayers().isEmpty());
    }

    @Override
    public void execute() {
        Keyboard.type("6", false);
    }
}
