package be.yurimoens.runemate.ccutter;

import be.yurimoens.runemate.ccutter.task.Cut;
import be.yurimoens.runemate.ccutter.task.Idle;
import be.yurimoens.runemate.ccutter.task.PickupNest;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CCutter extends TaskScript {

    @Override
    public void onStart(String... args) {
        setLoopDelay(600, 1000);

        GameEvents.get(GameEvents.RS3.UNEXPECTED_ITEM_HANDLER.getName()).disable();

        add(new PickupNest(), new Cut(), new Idle());
    }

}
