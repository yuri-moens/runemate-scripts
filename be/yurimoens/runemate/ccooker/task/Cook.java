package be.yurimoens.runemate.ccooker.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.framework.task.Task;

public class Cook extends Task {

    private final int[] RAW_FOOD = { 377, 383 };

    public Cook() {
        add(new ClickPortable(), new HandleCookingInterface());
    }

    @Override
    public boolean validate() {
        return (!Inventory.getItems(RAW_FOOD).isEmpty()
                && Interfaces.getAt(1251, 0) != null);
    }

    @Override
    public void execute() {
        getChildren().stream().forEach(task -> {
            if (task.validate()) task.execute();
        });
    }
}
