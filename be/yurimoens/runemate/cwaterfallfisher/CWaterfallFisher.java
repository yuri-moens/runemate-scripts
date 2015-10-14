package be.yurimoens.runemate.cwaterfallfisher;

import be.yurimoens.runemate.cwaterfallfisher.task.DestroyCracker;
import be.yurimoens.runemate.cwaterfallfisher.task.Fish;
import be.yurimoens.runemate.cwaterfallfisher.task.Idle;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CWaterfallFisher extends TaskScript {

    @Override
    public void onStart(String... args) {
        setLoopDelay(600, 1000);

        GameEvents.get(GameEvents.RS3.UNEXPECTED_ITEM_HANDLER.getName()).disable();

        add(new DestroyCracker(), new Fish(), new Idle());
    }

}
