package be.yurimoens.runemate.cfisher.task;

import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Filter;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class HandleBank extends Task {

    private final int[] URNS = { 20334, 20335, 20336 };

    @Override
    public boolean validate() {
        return (!Inventory.containsAnyOf(URNS) && Banks.getLoaded().nearest().distanceTo(Players.getLocal()) < 6);
    }

    @Override
    public void execute() {
        if (Bank.open()) {
            if (!Inventory.contains(new Filter<SpriteItem>() {
                @Override
                public boolean accepts(SpriteItem spriteItem) {
                    return spriteItem.getDefinition().getName().contains("Games necklace");
                }
            })) {
                Bank.withdraw(new Filter<SpriteItem>() {
                    @Override
                    public boolean accepts(SpriteItem spriteItem) {
                        return spriteItem.getDefinition().getName().contains("Games necklace");
                    }
                }, 1);
            }

            Bank.withdraw(new Filter<SpriteItem>() {
                @Override
                public boolean accepts(SpriteItem spriteItem) {
                    return spriteItem.getDefinition().getName().contains("Fishing urn");
                }
            }, 10);
        }

        Bank.close();

        Timer timeout = new Timer(2000L);
        timeout.start();
        while (timeout.isRunning() && Bank.isOpen()) {
            Execution.delay(0, 150);
        }
    }
}
