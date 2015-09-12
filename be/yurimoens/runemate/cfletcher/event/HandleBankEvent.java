package be.yurimoens.runemate.cfletcher.event;

import java.util.EventObject;

public class HandleBankEvent extends EventObject {

    public int bowsStrung, logsCut;

    public HandleBankEvent(Object source, int bowsStrung, int logsCut) {
        super(source);
        this.bowsStrung = bowsStrung;
        this.logsCut = logsCut;
    }
}
