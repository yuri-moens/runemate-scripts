package be.yurimoens.runemate.cthiever;

import be.yurimoens.runemate.cthiever.task.Lure;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CThiever extends TaskScript {

    @Override
    public void onStart(String... args) {
        setLoopDelay(600, 750);

        add(new Lure());
    }

}
