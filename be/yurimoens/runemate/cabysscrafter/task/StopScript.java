package be.yurimoens.runemate.cabysscrafter.task;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.event.CreateRunesEvent;
import be.yurimoens.runemate.cabysscrafter.event.CreateRunesListener;
import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.RuneScape;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.util.StopWatch;
import com.runemate.game.api.script.framework.AbstractScript;
import com.runemate.game.api.script.framework.task.Task;

import java.awt.*;

public class StopScript extends Task implements CreateRunesListener {

    private final int RUNES_TO_CRAFT;
    private final long TIME_TO_RUN;
    private final AbstractScript script;
    private final StopWatch runtime;

    private int runesCrafted;

    public StopScript(AbstractScript script, StopWatch runtime, int runesToCraft, long timeToRun) {
        this.script = script;
        this.runtime = runtime;
        this.RUNES_TO_CRAFT = runesToCraft;
        this.TIME_TO_RUN = timeToRun;
    }

    @Override
    public boolean validate() {
        return (runtime.getRuntime() >= TIME_TO_RUN || runesCrafted >= RUNES_TO_CRAFT
                || (Bank.isOpen() && (Bank.getQuantity(Constants.PURE_ESSENCE) == 0 || Bank.getQuantity(Constants.AMULET_OF_GLORY_CHARGED) == 0)));
    }

    @Override
    public void execute() {
        RuneScape.logout();
        ClientUI.sendTrayNotification("CAbyssCrafter has stopped after crafting " + runesCrafted + " runes which took " + runtime.getRuntimeAsString(), TrayIcon.MessageType.INFO);
        script.stop();
    }

    @Override
    public void createRunesEventReceived(CreateRunesEvent event) {
        runesCrafted += event.RUNES_CRAFTED;
    }
}
