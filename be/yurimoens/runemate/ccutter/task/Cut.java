package be.yurimoens.runemate.ccutter.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Cut extends Task {

    private GameObject ivy;

    @Override
    public boolean validate() {
        return Players.getLocal().getStanceId() != 18019 && (ivy = GameObjects.newQuery().names("Ivy").results().nearest()) != null;
    }

    @Override
    public void execute() {
        if (ivy.isVisible()) {
            CMouse.fastInteract(ivy, "Chop");

            Execution.delayUntil(() -> Players.getLocal().getStanceId() == 18019, 4000, 5000);
        }
    }
}
