package be.yurimoens.runemate.ccwafker.task;

import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class EnterLobby extends Task {

    private GameObject portal;

    @Override
    public boolean validate() {
        portal = GameObjects.newQuery().names("Guthix portal").results().first();
        return portal != null;
    }

    @Override
    public void execute() {
        if (!portal.isVisible()) {
            Camera.turnTo(portal);
        }
        CMouse.fastInteract(portal, "Enter");

        Execution.delayUntil(() -> portal == null || !portal.isValid(), 4000, 5000);
    }
}
