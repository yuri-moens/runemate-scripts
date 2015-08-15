package be.yurimoens.runemate.cabysscrafter.task.altar;

import be.yurimoens.runemate.cabysscrafter.Constants;
import be.yurimoens.runemate.cabysscrafter.event.CreateRunesEvent;
import be.yurimoens.runemate.cabysscrafter.event.CreateRunesListener;
import be.yurimoens.runemate.util.CExecution;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

class CreateRunes extends Task {

    private final int ALTAR_MODEL_ID;
    private final int RUNE_ID;
    private final CopyOnWriteArrayList<CreateRunesListener> listeners;

    private CreateRunes(int altarModelId, int runeId) {
        ALTAR_MODEL_ID = altarModelId;
        RUNE_ID = runeId;
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public CreateRunes(int altarModelId, int runeId, CreateRunesListener... listeners) {
        this(altarModelId, runeId);
        Collections.addAll(this.listeners, listeners);
    }

    @Override
    public boolean validate() {
        return (getParent().validate()
                && Inventory.contains(Constants.PURE_ESSENCE));

    }

    @Override
    public void execute() {
        GameObject altar = GameObjects.newQuery().models(ALTAR_MODEL_ID).results().first();

        if (altar != null) {
            CExecution.delayUntil(() -> CMouse.fastInteract(altar, "Craft-rune"), Random.nextInt(450, 650), 3600, 4000);

            Camera.concurrentlyTurnTo(320, 0.666D, 0.05D);
            CMouse.moveToMinimap();

            Execution.delayUntil(() -> !Inventory.contains(Constants.PURE_ESSENCE), 5000, 6000);
            fireCreateRunesEvent();
        }
    }

    private void fireCreateRunesEvent() {
        CreateRunesEvent event = new CreateRunesEvent(this, getRunesCrafted());

        for (CreateRunesListener listener : listeners) {
            listener.createRunesEventReceived(event);
        }
    }

    private int getRunesCrafted() {
        return Inventory.getQuantity(RUNE_ID);
    }
}
