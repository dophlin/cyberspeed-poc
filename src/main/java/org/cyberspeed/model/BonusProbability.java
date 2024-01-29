package org.cyberspeed.model;
import java.util.Map;

public class BonusProbability {
    private Map<String, Integer> symbols;

    public BonusProbability() {}

    public BonusProbability(Map<String, Integer> symbols) {
        this.symbols = symbols;
    }

    public Map<String, Integer> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Integer> symbols) {
        this.symbols = symbols;
    }
}
