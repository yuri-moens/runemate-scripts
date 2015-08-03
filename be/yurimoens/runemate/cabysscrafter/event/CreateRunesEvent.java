package be.yurimoens.runemate.cabysscrafter.event;

import java.util.EventObject;

public class CreateRunesEvent extends EventObject {

    public final int RUNES_CRAFTED;

    public CreateRunesEvent(Object source, int runesCrafted) {
        super(source);
        this.RUNES_CRAFTED = runesCrafted;
    }

}
