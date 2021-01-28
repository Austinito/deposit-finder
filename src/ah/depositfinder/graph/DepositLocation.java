package ah.depositfinder.graph;

public enum DepositLocation {
    SB("SB"),
    SEVEN_E("7E"),
    PTS("PTS"),
    CVS("CVS"),
    FD("FD");

    private final String name;

    DepositLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DepositLocation fromString(String name) {
        for (DepositLocation enumValue : DepositLocation.values()) {
            if (enumValue.name.equalsIgnoreCase(name)) {
                return enumValue;
            }
        }
        return null;
    }
}
