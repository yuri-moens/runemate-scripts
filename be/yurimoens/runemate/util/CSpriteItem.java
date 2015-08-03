package be.yurimoens.runemate.util;

import com.runemate.game.api.hybrid.local.hud.interfaces.SpriteItem;

public class CSpriteItem {

    public static String getName(SpriteItem spriteItem) {
        return spriteItem == null ? null : spriteItem.toString().split(",")[1].split("=")[1];
    }

}
