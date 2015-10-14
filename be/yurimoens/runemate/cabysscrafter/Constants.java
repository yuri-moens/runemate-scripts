package be.yurimoens.runemate.cabysscrafter;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

public class Constants {

    public static final int PURE_ESSENCE = 7936;
    public static final int AMULET_OF_GLORY_EMPTY = 1704;
    public static final int AMULET_OF_GLORY_CHARGED = 1712;
    public static final int GIANT_POUCH = 5514;

    public static final int MAGE_MODEL = 1722666346;
    public static final int DARK_MAGE_MODEL = 2091689888;

    public static final int TELEPORT_INTERFACE = 1188;

    public static final int[] DEGRADED_POUCH_IDS = { 5511, 5513, 5515 };
    public static final int[] OBSTACLES = { 7143, 7144, 7145, 7146, 7147, 7148, 7149, 7150, 7151, 7152, 7153 };
    public static final int[] WILDERNESS_WALLS = { -1852165344, -694400184, -181306661 };

    public static final Area.Circular mageArea = new Area.Circular(new Coordinate(3106, 3555, 0), 9.5D);
    public static final Area.Circular innerRing = new Area.Circular(new Coordinate(3039, 4832, 0), 17D);
    public static final Coordinate edgevilleTeleport = new Coordinate(3087, 3496, 0);
    public static final Coordinate tzhaarCaves = new Coordinate(4743, 5171, 0);
    public static final Coordinate tzhaarPlaza = new Coordinate(4676, 5155, 0);
    public static final Coordinate wildernessWall = new Coordinate(3103, 3521, 0);

}
