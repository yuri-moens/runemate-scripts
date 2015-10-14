package be.yurimoens.runemate.cabysscrafter.task.bank;

import be.yurimoens.runemate.cabysscrafter.Constants;
import com.runemate.game.api.hybrid.entities.LocatableEntity;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Camera;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.basic.PredefinedPath;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.calculations.Random;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class HandleBank extends Task {

    private LocatableEntity bank;

    public HandleBank(int preset) {
        add(new EquipGlory(), new WithdrawEssence(preset));
    }

    @Override
    public boolean validate() {
        return ((bank = getBank()) != null && !Inventory.contains(Constants.PURE_ESSENCE));
    }


    @Override
    public void execute() {
        if (!Bank.isOpen()) {
            if (!bank.isVisible()) {
                PredefinedPath path = PredefinedPath.create(
                        new Coordinate(3096, 3496, 0)
                );

                Execution.delayUntil(() -> {
                    path.step(true);
                    return bank.isVisible() || Players.getLocal().distanceTo(bank) <= 4D;
                }, 6000, 8000);
            }

            if (!bank.isVisible()) {
                Camera.turnTo(bank);
                Bank.open(bank, "Bank");
                Camera.concurrentlyTurnTo(320, 0.666D);
            } else {
                Bank.open(bank, "Bank");
            }
        }
    }

    private LocatableEntity getBank() {
        LocatableEntityQueryResults<Npc> bankers = Npcs.newQuery().filter(
                (npc) -> npc.getPosition().equals(new Coordinate(3097, 3494, 0)) || npc.getPosition().equals(new Coordinate(4742, 5172, 0))
        ).results();

        if (bankers.isEmpty() || Random.nextInt(2) == 1) {
            return GameObjects.newQuery().filter((gameObject) -> gameObject.getPosition().equals(new Coordinate(3097, 3495, 0))).results().nearest();
        }

        return bankers.first();
    }
}
