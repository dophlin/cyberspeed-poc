package org.cyberspeed;

import static org.junit.Assert.assertEquals;

import org.cyberspeed.model.*;
import org.junit.Test;

import java.util.*;

public class ScratchGameTest {
    @Test
    public void testWonScenario() {
        // Prepare the configuration object
        Configuration configuration = prepareConfigurationObject();
        // Initiate the betting amount
        int bettingAmount = 100;

        ScratchGame scratchGame = new ScratchGame(configuration, bettingAmount);
        scratchGame.overwriteMatrix(predefinedMatrixWon(), "+1000");
        OutputResponse outputResponse = scratchGame.handleGame();
        assertCheckOfWon(outputResponse);
    }

    @Test
    public void testLostScenario() {
        // Prepare the configuration object
        Configuration configuration = prepareConfigurationObject();
        // Initiate the betting amount
        int bettingAmount = 100;

        ScratchGame scratchGame = new ScratchGame(configuration, bettingAmount);
        scratchGame.overwriteMatrix(predefinedMatrixLost(), "5x");
        OutputResponse outputResponse = scratchGame.handleGame();
        assertCheckOfLost(outputResponse);
    }

    private Configuration prepareConfigurationObject() {
        Configuration configuration = new Configuration();
        configuration.setColumns(3);
        configuration.setRows(3);

        // ---- Setting symbols
        Map<String, Symbol> symbols = new HashMap<>();
        symbols.put("A", new Symbol(SymbolType.STANDARD, 50));
        symbols.put("B", new Symbol(SymbolType.STANDARD, 25));
        symbols.put("C", new Symbol(SymbolType.STANDARD, 10));
        symbols.put("D", new Symbol(SymbolType.STANDARD, 5));
        symbols.put("E", new Symbol(SymbolType.STANDARD, 3));
        symbols.put("F", new Symbol(SymbolType.STANDARD, 1.5));

        symbols.put("10x", new Symbol(SymbolType.BONUS, 10, "multiply_reward"));
        symbols.put("5x", new Symbol(SymbolType.BONUS, 5, "multiply_reward"));

        symbols.put("+1000", new Symbol(SymbolType.BONUS, "extra_bonus", 1000));
        symbols.put("+500", new Symbol(SymbolType.BONUS, "extra_bonus", 500));

        symbols.put("MISS", new Symbol(SymbolType.BONUS, "miss"));

        configuration.setSymbols(symbols);
        // ----

        // ---- Setting probabilities
        Probabilities probabilities = new Probabilities();
        List<StandardProbability> standardProbabilityList = new ArrayList<>();
        standardProbabilityList.add(new StandardProbability(0, 0, predefinedProbabilitiesForStandardSymbols()));
        standardProbabilityList.add(new StandardProbability(1, 0, predefinedProbabilitiesForStandardSymbols()));
        standardProbabilityList.add(new StandardProbability(2, 0, predefinedProbabilitiesForStandardSymbols()));
        standardProbabilityList.add(new StandardProbability(0, 1, predefinedProbabilitiesForStandardSymbols()));
        standardProbabilityList.add(new StandardProbability(1, 1, predefinedProbabilitiesForStandardSymbols()));
        standardProbabilityList.add(new StandardProbability(2, 1, predefinedProbabilitiesForStandardSymbols()));
        standardProbabilityList.add(new StandardProbability(0, 2, predefinedProbabilitiesForStandardSymbols()));
        standardProbabilityList.add(new StandardProbability(1, 2, predefinedProbabilitiesForStandardSymbols()));
        standardProbabilityList.add(new StandardProbability(2, 2, predefinedProbabilitiesForStandardSymbols()));
        probabilities.setStandard_symbols(standardProbabilityList);

        probabilities.setBonus_symbols(new BonusProbability(predefinedProbabilitiesForBonusSymbols()));

        configuration.setProbabilities(probabilities);
        // ----

        // ---- Setting win_combinations
        Map<String, WinCombination> winCombination = new HashMap<>();
        winCombination.put("same_symbol_3_times", new WinCombination(1, "same_symbols", 3, "same_symbols"));
        winCombination.put("same_symbol_4_times", new WinCombination(1.5, "same_symbols", 4, "same_symbols"));
        winCombination.put("same_symbol_5_times", new WinCombination(2, "same_symbols", 5, "same_symbols"));
        winCombination.put("same_symbol_6_times", new WinCombination(3, "same_symbols", 6, "same_symbols"));
        winCombination.put("same_symbol_7_times", new WinCombination(5, "same_symbols", 7, "same_symbols"));
        winCombination.put("same_symbol_8_times", new WinCombination(10, "same_symbols", 8, "same_symbols"));
        winCombination.put("same_symbol_9_times", new WinCombination(20, "same_symbols", 9, "same_symbols"));

        winCombination.put("same_symbols_horizontally", new WinCombination(2, "linear_symbols", "horizontally_linear_symbols",
                Arrays.asList(
                        Arrays.asList("0:0", "0:1", "0:2"),
                        Arrays.asList("1:0", "1:1", "1:2"),
                        Arrays.asList("2:0", "2:1", "2:2")
                )));

        winCombination.put("same_symbols_vertically", new WinCombination(2, "linear_symbols", "vertically_linear_symbols",
                Arrays.asList(
                        Arrays.asList("0:0", "1:0", "2:0"),
                        Arrays.asList("0:1", "1:1", "2:1"),
                        Arrays.asList("0:2", "1:2", "2:2")
                )));

        winCombination.put("same_symbols_diagonally_left_to_right", new WinCombination(5, "linear_symbols", "ltr_diagonally_linear_symbols",
                Collections.singletonList(
                        Arrays.asList("0:0", "1:1", "2:2")
                )));

        winCombination.put("same_symbols_diagonally_right_to_left", new WinCombination(5, "linear_symbols", "rtl_diagonally_linear_symbols",
                Collections.singletonList(
                        Arrays.asList("0:2", "1:1", "2:0")
                )));

        configuration.setWin_combinations(winCombination);
        // ----

        return configuration;
    }

    private Map<String, Integer> predefinedProbabilitiesForStandardSymbols() {
        Map<String, Integer> result = new HashMap<>();
        result.put("A", 1);
        result.put("B", 2);
        result.put("C", 3);
        result.put("D", 4);
        result.put("E", 5);
        result.put("F", 6);
        return result;
    }

    private Map<String, Integer> predefinedProbabilitiesForBonusSymbols() {
        Map<String, Integer> result = new HashMap<>();
        result.put("10x", 1);
        result.put("5x", 2);
        result.put("+1000", 3);
        result.put("+500", 4);
        result.put("MISS", 5);
        return result;
    }

    private Matrix predefinedMatrixWon() {
        Matrix matrix = new Matrix(3, 3);
        matrix.setValue(0, 0, "A");
        matrix.setValue(0, 1, "A");
        matrix.setValue(0, 2, "B");

        matrix.setValue(1, 0, "A");
        matrix.setValue(1, 1, "+1000");
        matrix.setValue(1, 2, "B");

        matrix.setValue(2, 0, "A");
        matrix.setValue(2, 1, "A");
        matrix.setValue(2, 2, "B");

        return matrix;
    }

    private void assertCheckOfWon(OutputResponse outputResponse) {
        // Assert that the getters return the expected values
        assertEquals("Reward value is not as expected", 26000, outputResponse.getReward());
        assertEquals("Applied bonus symbol is not as expected", "+1000", outputResponse.getAppliedBonusSymbol());

        Map<String, List<String>> expectedWinCombinations = new HashMap<>();
        expectedWinCombinations.put("A", Arrays.asList("same_symbol_5_times", "same_symbols_vertically"));
        expectedWinCombinations.put("B", Arrays.asList("same_symbol_3_times", "same_symbols_vertically"));


        for (String key : expectedWinCombinations.keySet()) {
            List<String> expectedList = expectedWinCombinations.get(key);
            List<String> actualList = outputResponse.getAppliedWinningCombinations().get(key);

            Collections.sort(expectedList);
            Collections.sort(actualList);

            assertEquals("Applied winning combinations are not as expected", expectedList, actualList);
        }
    }


    private Matrix predefinedMatrixLost() {
        Matrix matrix = new Matrix(3, 3);
        matrix.setValue(0, 0, "A");
        matrix.setValue(0, 1, "B");
        matrix.setValue(0, 2, "C");

        matrix.setValue(1, 0, "E");
        matrix.setValue(1, 1, "B");
        matrix.setValue(1, 2, "5x");

        matrix.setValue(2, 0, "F");
        matrix.setValue(2, 1, "D");
        matrix.setValue(2, 2, "C");

        return matrix;
    }

    private void assertCheckOfLost(OutputResponse outputResponse) {
        // Assert that the getters return the expected values
        assertEquals("Reward value is not as expected", 0, outputResponse.getReward());
    }
}
