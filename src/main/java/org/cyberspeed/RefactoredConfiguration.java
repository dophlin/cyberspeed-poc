package org.cyberspeed;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RefactoredConfiguration {
    private final Map<String, Integer>[][] standardSymbolWeights;
    private final Map<String, Integer> bonusSymbolWeights;
    private final Set<String> standardSymbols;

    public RefactoredConfiguration(Configuration configuration) {
        standardSymbolWeights = new HashMap[configuration.getRows()][configuration.getColumns()];
        bonusSymbolWeights = configuration.getProbabilities().getBonus_symbols().getSymbols();
        for (int i = 0; i < standardSymbolWeights.length; i++) {
            for (int j = 0; j < standardSymbolWeights[i].length; j++) {
                standardSymbolWeights[i][j] = new HashMap<>();
            }
        }

        standardSymbols = new HashSet<>();

        for(String symbol : configuration.getSymbols().keySet()) {
            if(configuration.getSymbols().get(symbol).getType().equals(SymbolType.STANDARD)) {
                standardSymbols.add(symbol);
            }
        }

        for(StandardProbability standardProbability : configuration.getProbabilities().getStandard_symbols()) {
            setStandardSymbolWeightsValue(standardProbability.getRow(), standardProbability.getColumn(), standardProbability.getSymbols());
        }
    }

    private void setStandardSymbolWeightsValue(int row, int col, Map<String, Integer> weights) {
        if (row >= 0 && row < standardSymbolWeights.length && col >= 0 && col < standardSymbolWeights[0].length) {
            standardSymbolWeights[row][col] = weights;
        } else {
            System.out.println("Invalid row or column index");
            throw new RuntimeException("InvalidConfiguration");
        }
    }

    public Integer getSumStandardSymbolWeights(int row, int col) {
        if (row >= 0 && row < standardSymbolWeights.length && col >= 0 && col < standardSymbolWeights[0].length) {
            Integer sumWeights = 0;
            for(Integer weight : standardSymbolWeights[row][col].values()) {
                sumWeights += weight;
            }
            return sumWeights;
        } else {
            System.out.println("Invalid row or column index");
            throw new RuntimeException("InvalidRowOrColumn");
        }
    }

    public Set<String> getStandardSymbols(int row, int col) {
        return standardSymbolWeights[row][col].keySet();
    }

    public Set<String> getStandardSymbols() {
        return standardSymbols;
    }

    public Integer getStandardSymbolWeight(int row, int col, String symbol) {
        if (row >= 0 && row < standardSymbolWeights.length && col >= 0 && col < standardSymbolWeights[0].length) {
            Integer weight = standardSymbolWeights[row][col].get(symbol);
            if(weight == null) throw new RuntimeException("InvalidSymbol");
            return weight;
        } else {
            System.out.println("Invalid row or column index");
            throw new RuntimeException("InvalidRowOrColumn");
        }
    }

    public Integer getSumBonusSymbolWeights() {
        Integer sumWeights = 0;
        for(Integer weight : bonusSymbolWeights.values()) {
            sumWeights += weight;
        }
        return sumWeights;
    }

    public Set<String> getBonusSymbols() {
        return bonusSymbolWeights.keySet();
    }

    public Integer getBonusSymbolWeight(String symbol) {
        return bonusSymbolWeights.get(symbol);
    }
}
