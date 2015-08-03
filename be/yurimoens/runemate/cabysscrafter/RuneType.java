package be.yurimoens.runemate.cabysscrafter;

public enum RuneType {

    AIR(556, 368318466, 7139),
    MIND(558, 932264649, 7140),
    WATER(555, 1872515823, 7137),
    EARTH(557, 1544907721, 7130),
    FIRE(554, -1304256332, 7129),
    BODY(559, -1979319427, 7131),
    COSMIC(564, 2052687296, 7132),
    CHAOS(562, -2022799313, 7134),
    NATURE(561, -13899977, 7133),
    LAW(563, -1108113842, 7135),
    DEATH(560, 1580229602, 7136),
    BLOOD(565, -1420270802, 7141);

    public final int RUNE_ID, ALTAR_MODEL_ID, RIFT_ID;

    RuneType(int runeId, int altarModelId, int riftId) {
        this.RUNE_ID = runeId;
        this.ALTAR_MODEL_ID = altarModelId;
        this.RIFT_ID = riftId;
    }

    @Override
    public String toString() {
        return this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
    }

}
