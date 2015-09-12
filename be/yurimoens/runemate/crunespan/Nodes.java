package be.yurimoens.runemate.crunespan;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.local.Skill;
import com.runemate.game.api.hybrid.queries.results.LocatableEntityQueryResults;
import com.runemate.game.api.hybrid.region.GameObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Nodes {

    enum Node {

        CYCLONE(0, 1),
        MIND_STORM(660208483, 1),
        WATER_POOL(13246974, 5),
        ROCK_FRAGMENT(-1821501047, 9),
        FIREBALL(-920376310, 14),
        VINE(564429197, 17),
        FLESHY_GROWTH(-1890292202, 20),
        FIRE_STORM(-1310174263, 27),
        CHAOTIC_CLOUD(-613290247, 35),
        NEBULA(721684161, 40),
        SHIFTER(316326937, 44),
        JUMPER(437388889, 54),
        SKULLS(-339053681, 65),
        BLOOD_POOL(297078748, 77),
        BLOODY_SKULLS(1191088118, 83),
        LIVING_SOUL(-546760512, 90),
        UNDEAD_SOUL(1749403329, 95);

        public final int MODEL_ID;
        public final int REQUIRED_LEVEL;

        Node(int modelId, int requiredLevel) {
            MODEL_ID = modelId;
            REQUIRED_LEVEL = requiredLevel;
        }

        public static int[] getUsableNodes() {
            int[] modelIds = new int[Node.values().length];
            int level = Skill.RUNECRAFTING.getCurrentLevel();
            int i = 0;

            for (Node essNpc : Node.values()) {
                if (essNpc.REQUIRED_LEVEL <= level) {
                    modelIds[i++] = essNpc.MODEL_ID;
                }
            }

            return modelIds;
        }

        public static int getRequiredLevel(int modelId) {
            for (Node essNpc : Node.values()) {
                if (essNpc.MODEL_ID == modelId) {
                    return essNpc.REQUIRED_LEVEL;
                }
            }

            return 0;
        }

    }

    static class NodeComparator implements Comparator<GameObject> {

        @Override
        public int compare(GameObject go1, GameObject go2) {
            return Node.getRequiredLevel(go2.getModel().hashCode()) - Node.getRequiredLevel(go1.getModel().hashCode());
        }
    }

    public static LocatableEntityQueryResults<GameObject> getNodes() {
        return getNodes(null);
    }

    public static LocatableEntityQueryResults<GameObject> getNodes(GameObject ignoreNode) {
        if (ignoreNode == null) {
            return GameObjects.newQuery().models(Node.getUsableNodes()).reachable().results();
        } else {
            return GameObjects.newQuery().models(Node.getUsableNodes()).filter((gameObject) -> !gameObject.equals(ignoreNode)).reachable().results();
        }
    }

    public static List<GameObject> getNodesSorted() {
        return getNodesSorted(null);
    }

    public static List<GameObject> getNodesSorted(GameObject ignoreNode) {
        List<GameObject> nodes = new ArrayList<>();
        nodes.addAll(getNodes(ignoreNode));

        Collections.sort(nodes, new NodeComparator());

        return nodes;
    }

    public static GameObject getNearestNode() {
        return getNearestNode(null);
    }

    public static GameObject getNearestNode(GameObject ignoreNode) {
        LocatableEntityQueryResults<GameObject> nodes = getNodes();
        if (nodes.isEmpty()) {
            return null;
        } else {
            return nodes.nearest();
        }
    }

    public static GameObject getHighestNode() {
        return getHighestNode(null);
    }

    public static GameObject getHighestNode(GameObject ignoreNode) {
        List<GameObject> nodes = getNodesSorted(ignoreNode);
        if (nodes == null || nodes.isEmpty()) {
            return null;
        } else {
            return nodes.get(0);
        }
    }

}
