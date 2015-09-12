package be.yurimoens.runemate.ccwafker.task;

import be.yurimoens.runemate.ccwafker.Constants;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.ChatDialog;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.framework.task.Task;

public class EnterFromLobby extends Task {

    private Timer timeout;

    @Override
    public boolean validate() {
        for (Area.Circular lobby : Constants.lobbies) {
            if (lobby.contains(Players.getLocal())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void execute() {
        ChatDialog.Option option = ChatDialog.getOption("Yes, please!");
        if (option != null) {
            option.select();
        }

        if (timeout == null || !timeout.isRunning()) {
            timeout = new Timer(Random.nextInt(240000));
            timeout.start();

            Camera.turnTo(Random.nextInt(360));
        }
    }
}
