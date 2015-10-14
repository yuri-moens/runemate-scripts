package be.yurimoens.runemate.cburner;

import be.yurimoens.runemate.cburner.task.Burn;
import be.yurimoens.runemate.cburner.task.HandleBank;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CBurner extends TaskScript {

    @Override
    public void onStart(String... args) {
        GameEvents.get(GameEvents.RS3.UNEXPECTED_ITEM_HANDLER.getName()).disable();

        setLoopDelay(600, 1000);

        add(new Burn(), new HandleBank());
    }

}
