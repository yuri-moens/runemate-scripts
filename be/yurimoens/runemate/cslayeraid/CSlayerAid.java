package be.yurimoens.runemate.cslayeraid;

import be.yurimoens.runemate.cslayeraid.task.Eat;
import be.yurimoens.runemate.cslayeraid.task.Pray;
import be.yurimoens.runemate.cslayeraid.task.TurnPrayerOff;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CSlayerAid extends TaskScript {

    @Override
    public void onStart(String... args) {
        setLoopDelay(100, 250);

        add(new Eat(), new Pray(), new TurnPrayerOff());
    }

}
