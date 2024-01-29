package org.cyberspeed;

public enum SymbolType {
    BONUS("bonus"),
    STANDARD("standard");

    private final String value;

    SymbolType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
