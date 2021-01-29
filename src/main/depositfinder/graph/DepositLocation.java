package main.depositfinder.graph;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private static final Map<String, DepositLocation> STRING_MAP = Arrays.stream(values()).collect(Collectors.toMap(DepositLocation::getName, Function.identity()));
    public static DepositLocation fromString(String name) {
        return STRING_MAP.get(name);
    }
}
