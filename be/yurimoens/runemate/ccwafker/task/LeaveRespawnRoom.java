package be.yurimoens.runemate.ccwafker.task;

import be.yurimoens.runemate.ccwafker.Constants;
import be.yurimoens.runemate.util.CMouse;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class LeaveRespawnRoom extends Task {

    private Area.Rectangular currentRoom;

    @Override
    public boolean validate() {
        for (Area.Rectangular waitingRoom : Constants.waitingRooms) {
            if (waitingRoom.contains(Players.getLocal())) {
                currentRoom = waitingRoom;
                return true;
            }
        }

        return false;
    }

    @Override
    public void execute() {
        if (Random.nextInt(2) == 1) {
            GameObject doors = GameObjects.newQuery().names("Doors").results().nearest();
            if (doors != null) {
                if (!doors.isVisible()) {
                    Camera.turnTo(doors);
                }

                CMouse.fastInteract(doors, "Pass");
            }
        } else {
            GameObject ladder = GameObjects.newQuery().filter((gameObject) -> gameObject.getId() == Constants.LADDER).results().nearest();
            if (ladder != null) {
                if (!ladder.isVisible()) {
                    Camera.turnTo(ladder);
                }

                CMouse.fastInteract(ladder, "Climb up");
            }
        }

        Execution.delayWhile(() -> currentRoom.contains(Players.getLocal()), 5000, 7000);
    }
}
