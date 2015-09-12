package be.yurimoens.runemate.cslayeraid.task;

import com.runemate.game.api.hybrid.entities.Projectile;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.region.Projectiles;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.task.Task;

public class Pray extends Task {

    private LocatableEntityQueryResults<Projectile> projectiles;

    @Override
    public boolean validate() {
        return (!(projectiles = Projectiles.newQuery().target(Players.getLocal()).results()).isEmpty());
    }

    @Override
    public void execute() {
        Keyboard.type("6", false);

        Execution.delayUntil(() -> projectiles.first() == null || !projectiles.first().isValid(), 2000, 3000);
        int startHealth = Health.getCurrent();

        Execution.delayUntil(() -> Health.getCurrent() < startHealth, 300, 700);

        Keyboard.type("6", false);
    }
}
