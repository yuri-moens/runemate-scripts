package be.yurimoens.runemate.charper;

import be.yurimoens.runemate.charper.task.Play;
import be.yurimoens.runemate.charper.task.Tune;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CHarper extends TaskScript {

    @Override
    public void onStart(String... args) {
        setLoopDelay(600, 1000);

        GameEvents.get(GameEvents.RS3.UNEXPECTED_ITEM_HANDLER.getName()).disable();

        add(new Play(), new Tune());
    }

}
