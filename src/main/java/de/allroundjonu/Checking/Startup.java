package de.allroundjonu.Checking;

import de.allroundjonu.Utils.CustomLogger;
import de.allroundjonu.Utils.BotJSONParser;

public class Startup {


    public static void startUP() throws InterruptedException {

        CustomLogger.log("message", "" +
                "\n   _____ _______       _____ _______ _    _ _____  \n" +
                " / ____|__   __|/\\   |  __ \\__   __| |  | |  __ \\ \n" +
                "| (___    | |  /  \\  | |__) | | |  | |  | | |__) |\n" +
                " \\___ \\   | | / /\\ \\ |  _  /  | |  | |  | |  ___/ \n" +
                " ____) |  | |/ ____ \\| | \\ \\  | |  | |__| | |     \n" +
                "|_____/   |_/_/    \\_\\_|  \\_\\ |_|   \\____/|_|     \n" +
                "                                                  \n" +
                "                                                  ");

        CustomLogger.log("message", "Checking if all necessary files exist...");
        if (!BotJSONParser.checkSettingFiles()){
            CustomLogger.log("error", "Bot wird gestoppt!");
            return;
        }
        CustomLogger.log("message", "Parsing .json files...");
        if (!BotJSONParser.startupJSONParser()) {
            CustomLogger.log("error", "Bot wird gestoppt!");
            return;
        }
        CustomLogger.log("message", "Checking Database settings...");

        System.out.println("Der Bot startet jetzt (Das kann ein bisschen Dauern)");
        System.out.print("LOADING: 0%");
        Thread.sleep(1000);
        System.out.print("\b\b");
        System.out.print("5%");
        System.out.println("  ");


    }




}
