package org.cyberspeed;

import java.util.*;

public class ScratchGame {
    private final Configuration configuration;
    private final Integer bettingAmount;
    private final Matrix matrix;

    private final RefactoredConfiguration refactoredConfiguration;

    private static class WinCombinationPair {
        String winCombinationName;
        double rewardMultiplier;

        public WinCombinationPair(String name, double rewardMultiplier) {
            this.winCombinationName = name;
            this.rewardMultiplier = rewardMultiplier;
        }
    }

    public ScratchGame(Configuration configuration, Integer bettingAmount) {
        this.configuration = configuration;
        this.bettingAmount = bettingAmount;
        matrix = new Matrix(this.configuration.getRows(), this.configuration.getColumns());
        refactoredConfiguration = new RefactoredConfiguration(this.configuration);
    }

    public void handleGame() {
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                generateSymbolForCell(i, j);
            }
        }

        String selectedBonusSymbol = generateBonusSymbol();
        Integer[] selectedBonusCell = selectBonusCell();
        matrix.setValue(selectedBonusCell[0], selectedBonusCell[1], selectedBonusSymbol);

//        matrix.fillTestMatrix();

        Map<String, Collection<WinCombinationPair>> appliedWinningCombinations = calculateWinningCombinations();

        Double finalReward = calculateFinalReward(appliedWinningCombinations, selectedBonusSymbol);

        matrix.printMatrix();
        System.out.println("Let see: " + finalReward);
    }

    private void generateSymbolForCell(int row, int col) {
        Integer totalProbability = refactoredConfiguration.getSumStandardSymbolWeights(row, col);
        String[] extendedProbArea = new String[totalProbability];
        int lastWrittenIndex = 0;
        for (String symbol : refactoredConfiguration.getStandardSymbols(row, col)) {
            Integer weight = refactoredConfiguration.getStandardSymbolWeight(row, col, symbol);
            for (int index = 0; index < weight; index++) {
                extendedProbArea[lastWrittenIndex++] = symbol;
            }
        }
        Random random = new Random();
        int selectRandomSymbolIndex = random.nextInt(totalProbability);
        String selectedSymbol = extendedProbArea[selectRandomSymbolIndex];
        matrix.setValue(row, col, selectedSymbol);
    }

    private String generateBonusSymbol() {
        Integer totalProbability = refactoredConfiguration.getSumBonusSymbolWeights();
        String[] extendedProbArea = new String[totalProbability];
        int lastWrittenIndex = 0;
        for (String symbol : refactoredConfiguration.getBonusSymbols()) {
            Integer weight = refactoredConfiguration.getBonusSymbolWeight(symbol);
            for (int index = 0; index < weight; index++) {
                extendedProbArea[lastWrittenIndex++] = symbol;
            }
        }
        Random random = new Random();
        int selectRandomSymbolIndex = random.nextInt(totalProbability);
        return extendedProbArea[selectRandomSymbolIndex];
    }

    private Integer[] selectBonusCell() {
        Integer[] ij = new Integer[2];
        Random random = new Random();
        ij[0] = random.nextInt(configuration.getRows());
        ij[1] = random.nextInt(configuration.getColumns());
        return ij;
    }

    private Map<String, Collection<WinCombinationPair>> calculateWinningCombinations() {
        Map<String, Collection<WinCombinationPair>> winningCombinations = new HashMap<>();
        for (String symbol : refactoredConfiguration.getStandardSymbols()) {
            winningCombinations.put(symbol, calculateWinningCombinationsForSymbol(symbol));
        }
        return winningCombinations;
    }
    private Collection<WinCombinationPair> calculateWinningCombinationsForSymbol(String symbol) {
        Map<String, WinCombinationPair> winCombinationGroups = new HashMap<>();
        for (String winCombinationName : configuration.getWin_combinations().keySet()) {
            WinCombination winCombination = configuration.getWin_combinations().get(winCombinationName);
            switch (winCombination.getGroup()) {
                case "same_symbols":
                    double sameSymbolsRewardMultiplier = calculateWinningCombinationsForSameSymbols(symbol, winCombination);
                    if (sameSymbolsRewardMultiplier == 0) continue;
                    if (winCombinationGroups.get(winCombination.getGroup()) != null) {
                        if (winCombinationGroups.get(winCombination.getGroup()).rewardMultiplier >= sameSymbolsRewardMultiplier)
                            continue;
                    }
                    winCombinationGroups.put(winCombination.getGroup(), new WinCombinationPair(winCombinationName, sameSymbolsRewardMultiplier));
                    break;
                case "horizontally_linear_symbols":
                case "vertically_linear_symbols":
                case "ltr_diagonally_linear_symbols":
                case "rtl_diagonally_linear_symbols":
                    double linearSymbolsRewardMultiplier = calculateWinningCombinationsForLinear(symbol, winCombination);
                    if (linearSymbolsRewardMultiplier == 0) continue;
                    winCombinationGroups.put(winCombination.getGroup(), new WinCombinationPair(winCombinationName, linearSymbolsRewardMultiplier));
                    break;
                default:
                    throw new RuntimeException("InvalidWinCombinationGroupName");
            }
        }
        return winCombinationGroups.values();
    }

    private double calculateWinningCombinationsForSameSymbols(String symbol, WinCombination winCombination) {
        int appearanceCount = 0;
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                if (matrix.getValue(i, j).equals(symbol)) appearanceCount++;
            }
        }
        if (appearanceCount == winCombination.getCount()) return winCombination.getReward_multiplier();
        return 0;
    }

    private double calculateWinningCombinationsForLinear(String symbol, WinCombination winCombination) {
        boolean isMatched = false;
        for (List<String> coveredArea : winCombination.getCovered_areas()) {
            if (isMatched) return winCombination.getReward_multiplier();
            isMatched = patternCheck(coveredArea, symbol);
        }
        return isMatched ? winCombination.getReward_multiplier() : 0;
    }

    private boolean patternCheck(List<String> coveredArea, String symbol) {
        boolean hasFailed = false;
        for (String s : coveredArea) {
            String[] indexes = s.split(":");
            if (indexes.length != 2) throw new RuntimeException("InvalidCoveredArea");
            try {
                int row = Integer.parseInt(indexes[0]);
                int col = Integer.parseInt(indexes[1]);
                hasFailed = !matrix.getValue(row, col).equals(symbol);
                if(hasFailed) return false;
            } catch (NumberFormatException e) {
                throw new RuntimeException("CoveredAreaIndexNumberFormatException");
            }
        }
        return true;
    }

    private Double calculateFinalReward(Map<String, Collection<WinCombinationPair>> appliedWinningCombinations, String appliedBonusSymbol) {
        double finalReward = 0;
        for (String symbol : appliedWinningCombinations.keySet()) {
            finalReward += calculateWinningCombination(symbol, appliedWinningCombinations.get(symbol));
        }
        if (finalReward > 0) {
            finalReward = calculateExtraBonus(finalReward, appliedBonusSymbol);
        }
        return finalReward;
    }

    private double calculateWinningCombination(String symbol, Collection<WinCombinationPair> winningCombinations) {
        if(winningCombinations.isEmpty()) return 0;
        double pureReward = bettingAmount * configuration.getSymbols().get(symbol).getReward_multiplier();
        for (WinCombinationPair winningCombination : winningCombinations) {
            pureReward *= winningCombination.rewardMultiplier;
        }
        return pureReward;
    }

    private Double calculateExtraBonus(Double finalReward, String appliedBonusSymbol) {
        switch (configuration.getSymbols().get(appliedBonusSymbol).getImpact()) {
            case "multiply_reward":
                return finalReward * configuration.getSymbols().get(appliedBonusSymbol).getReward_multiplier();
            case "extra_bonus":
                return finalReward + configuration.getSymbols().get(appliedBonusSymbol).getExtra();
            case "miss":
                return finalReward;
            default:
                throw new RuntimeException("InvalidImpact");
        }
    }
}
