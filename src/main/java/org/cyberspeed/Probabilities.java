package org.cyberspeed;
import java.util.List;

public class Probabilities {
    private List<StandardProbability> standard_symbols;
    private BonusProbability bonus_symbols;

    public List<StandardProbability> getStandard_symbols() {
        return standard_symbols;
    }

    public void setStandard_symbols(List<StandardProbability> standard_symbols) {
        this.standard_symbols = standard_symbols;
    }

    public BonusProbability getBonus_symbols() {
        return bonus_symbols;
    }

    public void setBonus_symbols(BonusProbability bonus_symbols) {
        this.bonus_symbols = bonus_symbols;
    }
}
