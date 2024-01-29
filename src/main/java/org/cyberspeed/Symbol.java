package org.cyberspeed;

public class Symbol {
    private Double reward_multiplier;
    private SymbolType type;
    private String impact;
    private Double extra;

    public Double getReward_multiplier() {
        return reward_multiplier;
    }

    public void setReward_multiplier(Double reward_multiplier) {
        this.reward_multiplier = reward_multiplier;
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(String type) {
        if(type.equals("bonus")) this.type = SymbolType.BONUS;
        else if(type.equals("standard")) this.type = SymbolType.STANDARD;
        else throw new IllegalArgumentException("InvalidSymbolType");
    }

    public void setType(SymbolType type) {
        this.type = type;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }

    public Double getExtra() {
        return extra;
    }

    public void setExtra(Double extra) {
        this.extra = extra;
    }
}
