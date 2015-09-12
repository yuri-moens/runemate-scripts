package be.yurimoens.runemate.ccooker.event;

import java.util.EventObject;

public class HandleBankEvent extends EventObject {

    public int foodCooked;

    public HandleBankEvent(Object source, int foodCooked) {
        super(source);
        this.foodCooked = foodCooked;
    }

}
