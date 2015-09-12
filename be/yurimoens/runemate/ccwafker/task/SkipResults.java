package be.yurimoens.runemate.ccwafker.task;

import be.yurimoens.runemate.ccwafker.Constants;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class SkipResults extends Task {

    private InterfaceComponent closeButton;

    @Override
    public boolean validate() {
        closeButton = Interfaces.getAt(Constants.RESULT_INTERFACE, Constants.CLOSE_BUTTON);
        return closeButton != null;
    }

    @Override
    public void execute() {
        closeButton.click();

        Execution.delayUntil(() -> closeButton == null || !closeButton.isValid(), 3000, 4000);
    }
}
