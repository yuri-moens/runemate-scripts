package be.yurimoens.runemate.cabysscrafter;

import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.region.Players;

public enum Location {

    EDGEVILLE,
    WILDERNESS,
    MAGE_AREA,
    ABYSS,
    INNER_RING,
    ALTAR,
    TZHAAR_CAVES,
    TZHAAR_PLAZA;

    public static Location getLocation() {
        Player player = Players.getLocal();

        if (player.distanceTo(Constants.tzhaarCaves) < 10D) {
            return Location.TZHAAR_CAVES;
        } else if (player.distanceTo(Constants.tzhaarPlaza) < 10D) {
            return Location.TZHAAR_PLAZA;
        } else if (player.getPosition().getY() < Constants.wildernessWall.getY() && player.distanceTo(Constants.wildernessWall) < 50D) {
            return Location.EDGEVILLE;
        } else if (Constants.mageArea.contains(player)) {
            return Location.MAGE_AREA;
        } else if (player.getPosition().getY() >= Constants.wildernessWall.getY() && player.distanceTo(Constants.wildernessWall) < 50D) {
            return Location.WILDERNESS;
        } else if (Constants.innerRing.contains(player)) {
            return Location.INNER_RING;
        } else if (player.distanceTo(Constants.innerRing.getCenter()) < 35D) {
            return Location.ABYSS;
        } else {
            return Location.ALTAR;
        }
    }

}
