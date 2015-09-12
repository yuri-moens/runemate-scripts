package be.yurimoens.runemate.cdivination.task;

import be.yurimoens.runemate.cdivination.CDivination;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.script.framework.task.Task;

public class HandInChronicles extends Task {

    private final Coordinate DIVINATION_SPOT;

    public HandInChronicles(Coordinate divinationSpot) {
        DIVINATION_SPOT = divinationSpot;
    }

    @Override
    public boolean validate() {
        return CDivination.handInFragments;
    }

    @Override
    public void execute() {
        CDivination.handInFragments = false;
    }
}
