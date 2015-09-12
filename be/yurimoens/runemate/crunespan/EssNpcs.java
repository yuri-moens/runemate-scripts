package be.yurimoens.runemate.crunespan;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.Npcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EssNpcs {

    enum EssNpc {

        AIR_ESSLING(-1211094672, 1),
        MIND_ESSLING(1813642645, 1),
        WATER_ESSLING(1264472700, 5),
        EARTH_ESSLING(809086236, 9),
        FIRE_ESSLING(1818753889, 14),
        BODY_ESSHOUND(-1630328087, 20),
        COSMIC_ESSHOUND(932153520, 27),
        CHAOS_ESSHOUND(-447935700, 35),
        ASTRAL_ESSHOUND(-995408090, 40),
        NATURE_ESSHOUND(-1972326967, 44),
        LAW_ESSHOUND(-1145805361, 54),
        DEATH_ESSWRAITH(1256803819, 65),
        BLOOD_ESSWRAITH(794525716, 77),
        SOUL_ESSWRAITH(475532685, 90);

        public final int MODEL_ID;
        public final int REQUIRED_LEVEL;

        EssNpc(int modelId, int requiredLevel) {
            MODEL_ID = modelId;
            REQUIRED_LEVEL = requiredLevel;
        }

        public static int[] getUsableEssNpcs() {
            int[] modelIds = new int[EssNpc.values().length];
            int level = Skill.RUNECRAFTING.getCurrentLevel();
            int i = 0;

            for (EssNpc essNpc : EssNpc.values()) {
                if (essNpc.REQUIRED_LEVEL <= level) {
                    modelIds[i++] = essNpc.MODEL_ID;
                }
            }

            return modelIds;
        }

        public static int getRequiredLevel(int modelId) {
            for (EssNpc essNpc : EssNpc.values()) {
                if (essNpc.MODEL_ID == modelId) {
                    return essNpc.REQUIRED_LEVEL;
                }
            }

            return 0;
        }
    }

    static class EssNpcComparator implements Comparator<Npc> {

        @Override
        public int compare(Npc npc1, Npc npc2) {
            return EssNpc.getRequiredLevel(npc2.getModel().hashCode()) - EssNpc.getRequiredLevel(npc1.getModel().hashCode());
        }
    }

    public static LocatableEntityQueryResults<Npc> getEssNpcs() {
        return getEssNpcs(null);
    }

    public static LocatableEntityQueryResults<Npc> getEssNpcs(Npc ignoreNpc) {
        if (ignoreNpc == null) {
            return Npcs.newQuery().models(EssNpc.getUsableEssNpcs()).reachable().results();
        } else {
            return Npcs.newQuery().models(EssNpc.getUsableEssNpcs()).filter((npc) -> !npc.equals(ignoreNpc)).reachable().results();
        }
    }

    public static List<Npc> getEssNpcsSorted() {
        return getEssNpcsSorted(null);
    }

    public static List<Npc> getEssNpcsSorted(Npc ignoreNpc) {
        List<Npc> essNpcs = new ArrayList<>();
        essNpcs.addAll(getEssNpcs(ignoreNpc));

        Collections.sort(essNpcs, new EssNpcComparator());

        return essNpcs;
    }

    public static Npc getNearestEssNpc() {
        return getNearestEssNpc(null);
    }

    public static Npc getNearestEssNpc(Npc ignoreNpc) {
        LocatableEntityQueryResults<Npc> essNpcs = getEssNpcs();
        if (essNpcs.isEmpty()) {
            return null;
        } else {
            return essNpcs.nearest();
        }
    }

    public static Npc getHighestEssNpc() {
        return getHighestEssNpc(null);
    }

    public static Npc getHighestEssNpc(Npc ignoreNpc) {
        List<Npc> essNpcs = getEssNpcsSorted(ignoreNpc);
        if (essNpcs == null || essNpcs.isEmpty()) {
            return null;
        } else {
            return essNpcs.get(0);
        }
    }

}
