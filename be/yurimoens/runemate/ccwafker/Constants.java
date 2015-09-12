package be.yurimoens.runemate.ccwafker;

import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;

public class Constants {

    public static final int RESULT_INTERFACE = 985;
    public static final int CLOSE_BUTTON = 89;
    public static final int LADDER = 1856040666;

    public static final Area.Circular[] lobbies = { new Area.Circular(new Coordinate(2380, 9486, 0), 14), new Area.Circular(new Coordinate(2420, 9522, 0), 14) };
    public static final Area.Circular[] gameAreas = {
            new Area.Circular(new Coordinate(2425, 3077, 1), 10),
            new Area.Circular(new Coordinate(2425, 3077, 2), 10),
            new Area.Circular(new Coordinate(2373, 3130, 1), 10),
            new Area.Circular(new Coordinate(2373, 3130, 2), 10)
    };

    public static final Area.Rectangular[] waitingRooms = {
            new Area.Rectangular(new Coordinate(2423, 3072, 1), new Coordinate(2431, 3080, 1)),
            new Area.Rectangular(new Coordinate(2368, 3127, 1), new Coordinate(2376, 3135, 1))
    };

}
