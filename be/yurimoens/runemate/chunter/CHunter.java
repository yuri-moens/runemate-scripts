package be.yurimoens.runemate.chunter;

import be.yurimoens.runemate.chunter.task.CheckDroppedTrap;
import be.yurimoens.runemate.chunter.task.DismantleTrap;
import be.yurimoens.runemate.chunter.task.LayTrap;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.script.framework.task.TaskScript;

public class CHunter extends TaskScript {

    @Override
    public void onStart(String... args) {
        GameEvents.get(GameEvents.RS3.UNEXPECTED_ITEM_HANDLER.getName()).disable();

        add(new CheckDroppedTrap(), new LayTrap(3), new DismantleTrap());
    }

}
