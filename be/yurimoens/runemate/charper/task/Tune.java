package be.yurimoens.runemate.charper.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Tune extends Task {

    private GameObject harp;

    @Override
    public boolean validate() {
        harp = GameObjects.newQuery().filter((GameObject gameObject) -> {
            if (gameObject.getDefinition().getLocalState() != null) {
                return gameObject.getDefinition().getLocalState().getActions().contains("Tune");
            }

            return false;
        }).results().nearest();

        return harp != null && harp.distanceTo(Players.getLocal()) <= 2D;
    }

    @Override
    public void execute() {
        CMouse.fastInteract(harp, "Tune");

        Execution.delay(1800, 2400);
    }
}
