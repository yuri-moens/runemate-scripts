package be.yurimoens.runemate.csmelter;

import be.yurimoens.runemate.csmelter.task.HandleBank;
import be.yurimoens.runemate.csmelter.task.Smelt;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CSmelter extends TaskScript {

    @Override
    public void onStart(String... args) {
        setLoopDelay(400, 800);

        add(new HandleBank(), new Smelt());
    }

}
