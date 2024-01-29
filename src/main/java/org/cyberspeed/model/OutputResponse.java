package org.cyberspeed.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class OutputResponse {
    private final String[][] matrix;
    private final int reward;
    @JsonProperty("applied_winning_combinations")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<String, List<String>> appliedWinningCombinations;
    @JsonProperty("applied_bonus_symbol")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final String appliedBonusSymbol;

    public OutputResponse(String[][] matrix, int reward, Map<String, List<String>> appliedWinningCombinations, String appliedBonusSymbol) {
        this.matrix = matrix;
        this.reward = reward;
        this.appliedWinningCombinations = appliedWinningCombinations;
        this.appliedBonusSymbol = appliedBonusSymbol;
    }

    public String[][] getMatrix() {
        return matrix;
    }

    public int getReward() {
        return reward;
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public String getAppliedBonusSymbol() {
        return appliedBonusSymbol;
    }
}
