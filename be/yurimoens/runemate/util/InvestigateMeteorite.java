package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.location.navigation.cognizant.RegionPath;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.Timer;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import com.runemate.game.api.script.framework.task.Task;

import java.util.ArrayList;

public class InvestigateMeteorite extends Task implements ChatboxListener {

    public enum Skill{
        ATTACK(14),
        CONSTITUTION(15),
        MINING(16),
        STRENGTH(17),
        AGILITY(18),
        SMITHING(19),
        DEFENCE(20),
        HERBLORE(21),
        FISHING(22),
        RANGED(23),
        THIEVING(24),
        COOKING(25),
        PRAYER(26),
        CRAFTING(27),
        FIREMAKING(28),
        MAGIC(29),
        FLETCHING(30),
        WOODCUTTING(31),
        RUNECRAFTING(32),
        SLAYER(44),
        FARMING(45),
        CONSTRUCTION(47),
        HUNTER(54),
        SUMMONING(56),
        DUNGEONEERING(59),
        DIVINATION(63);

        public final int INDEX;

        Skill(int index) {
            this.INDEX = index;
        }

        @Override
        public String toString() {
            return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
        }
    }

    private final Skill SKILL;
    private final ArrayList<Npc> investigatedMeteorites;

    private LocatableEntityQueryResults<Npc> meteorites;
    private Timer timeout;

    public InvestigateMeteorite(Skill skill)  {
        investigatedMeteorites = new ArrayList<>();
        this.SKILL = skill;
    }

    @Override
    public boolean validate() {
        for (int i = 0; i < investigatedMeteorites.size(); i++) {
            if (!investigatedMeteorites.get(i).isValid()) {
                investigatedMeteorites.remove(i);
                i--;
            }
        }

        meteorites = Npcs.newQuery().filter((meteorite) -> {
            boolean accept = false;
            if (meteorite.getId() == 21086 && meteorite.isVisible()) {
                accept = true;
                for (Npc investigatedMeteorite : investigatedMeteorites) {
                    if (investigatedMeteorite.getPosition().equals(meteorite.getPosition())) {
                        accept = false;
                        break;
                    }
                }
            }
            return accept;
        }).results();

        return !meteorites.isEmpty();
    }

    @Override
    public void execute() {
        Coordinate startPosition = Players.getLocal().getPosition();
        Npc meteorite = meteorites.nearest();
        investigatedMeteorites.add(meteorite);

        do {
            CMouse.fastInteract(meteorite, "Investigate");
            Execution.delay(800, 1300);
        } while (Players.getLocal().getAnimationId() != -1);

        timeout = new Timer(8000L);
        timeout.start();
        boolean hasInterface = false;
        while (timeout.isRunning() && !hasInterface) {
            hasInterface = Interfaces.getAt(1263, 0) != null;
            Execution.delay(150, 250);
        }

        Execution.delay(260, 570);

        if (hasInterface) {
            Interfaces.getAt(1263, SKILL.INDEX).click();
            Execution.delay(0, 200);
            Interfaces.getAt(1263, 69).click();
        } else {
            return;
        }

        if (Players.getLocal().distanceTo(startPosition) > 5) {
            Execution.delay(0, 350);

            if (startPosition.isVisible()) {
                startPosition.click();
            } else {
                RegionPath.buildTo(startPosition).step(true);
            }

            timeout.reset();
            timeout.start();

            while (Players.getLocal().distanceTo(startPosition) > 5 && timeout.isRunning()) {
                Execution.delay(0, 350);
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getType() == Chatbox.Message.Type.SERVER
                && (messageEvent.getMessage().startsWith("Whatever this thing") || messageEvent.getMessage().startsWith("You've already claimed"))) {
            if (timeout != null) {
                timeout.stop();
            }
        }
    }
}
