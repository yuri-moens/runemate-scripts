package be.yurimoens.runemate.csuperheater;

import be.yurimoens.runemate.csuperheater.task.HandleBank;
import be.yurimoens.runemate.csuperheater.task.Superheat;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CSuperheater extends TaskScript {

    @Override
    public void onStart(String... args) {
        setLoopDelay(600, 1000);

        GameEvents.get(GameEvents.RS3.UNEXPECTED_ITEM_HANDLER.getName()).disable();

        add(new HandleBank(), new Superheat());
    }

}
