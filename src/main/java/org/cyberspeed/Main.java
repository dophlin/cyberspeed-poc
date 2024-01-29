package org.cyberspeed;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static Configuration _configuration;
    private static ScratchGame scratchGame = null;
    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");
        Main.handleArgs(args);
        if(scratchGame != null) scratchGame.handleGame();
    }

    private static void handleArgs(String[] args) {
        Map<String, String> arguments = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--config":
                    if (i + 1 < args.length) {
                        arguments.put("config", args[++i]);
                    }
                    break;
                case "--betting-amount":
                    if (i + 1 < args.length) {
                        arguments.put("betting-amount", args[++i]);
                    }
                    break;
            }
        }

        String configFilePath = arguments.get("config");
        String bettingAmountStr = arguments.get("betting-amount");
        int bettingAmount = 0;
        if (configFilePath == null) {
            System.out.println("InvalidConfigFilePath");
            return;
        }
        if (bettingAmountStr == null) {
            System.out.println("BettingAmountNotFound");
        } else {
            try {
                bettingAmount = Integer.parseInt(bettingAmountStr);
            } catch (NumberFormatException e) {
                System.out.println("InvalidBettingAmount");
            }
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            Configuration configuration = mapper.readValue(Paths.get(configFilePath).toFile(), Configuration.class);
            scratchGame = new ScratchGame(configuration, bettingAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}