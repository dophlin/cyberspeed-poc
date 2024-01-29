package org.cyberspeed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyberspeed.model.*;

import java.util.*;

public class ScratchGame {
    private final Configuration configuration; // The original input configuration
    private final Integer bettingAmount; // The input betting amount
    private final Matrix matrix; // The generated matrix

    private final RefactoredConfiguration refactoredConfiguration; // For convenient we calculate some values into refactoredConfiguration object

    // For more readability a Pair class is defined for storing winCombination name and reward
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
        // Generating the matrix and filling its cells with standard symbols based on the symbols properties
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                generateSymbolForCell(i, j);
            }
        }

        String selectedBonusSymbol = generateBonusSymbol(); // Select a bonus symbol based on the symbol properties
        Integer[] selectedBonusCell = selectBonusCell(); // Randomly select a cell for bonus symbol
        matrix.setValue(selectedBonusCell[0], selectedBonusCell[1], selectedBonusSymbol);


        // The winning combinations values are calculated by here
        Map<String, Collection<WinCombinationPair>> appliedWinningCombinations = calculateWinningCombinations();

        // Based on the calculated winning combinations values, the final reward value is calculated by here
        Double finalReward = calculateFinalReward(appliedWinningCombinations, selectedBonusSymbol);

        // Preparing the output response to be print later
        Map<String, List<String>> appliedWinningCombinationsNames = new HashMap<>();
        for(String symbol : appliedWinningCombinations.keySet()) {
            List<String> winCombinationNames = new ArrayList<>();
            for(WinCombinationPair winCombinationPair : appliedWinningCombinations.get(symbol)) {
                winCombinationNames.add(winCombinationPair.winCombinationName);
            }
            if(winCombinationNames.isEmpty()) continue;
            appliedWinningCombinationsNames.put(symbol, winCombinationNames);
        }
        if (finalReward == 0) selectedBonusSymbol = null; // If it is a lost, there should be 'applied_winning_combinations' and 'applied_bonus_symbol' in the output

        OutputResponse outputResponse = new OutputResponse(matrix.getMatrix(), (int) Math.floor(finalReward), appliedWinningCombinationsNames, selectedBonusSymbol);
        printJson(outputResponse); // Printing the output response in the console
    }

    private void printJson(OutputResponse output) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
            System.out.println(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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
            // Calculating the winning combinations for each symbol
            winningCombinations.put(symbol, calculateWinningCombinationsForSymbol(symbol));
        }
        return winningCombinations;
    }

    // There are 5 different group of the winning combinations, here it is looped over each group
    // and selects the highest reward winning combination of each group.
    // (Practically, only 'same_symbols' group has several winning combinations, other groups are only applicable once)
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

    // This algorithm considers the coveredArea input
    // and checks if the selected symbol is repeated based on the coveredArea or not
    private boolean patternCheck(List<String> coveredArea, String symbol) {
        for (String s : coveredArea) {
            String[] indexes = s.split(":");
            if (indexes.length != 2) throw new RuntimeException("InvalidCoveredArea");
            try {
                int row = Integer.parseInt(indexes[0]);
                int col = Integer.parseInt(indexes[1]);
                boolean hasFailed = !matrix.getValue(row, col).equals(symbol);
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
