package de.allroundjonu.Utils;

public class CustomLogger {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public static void log(String type, String message){

        switch (type) {
            case "error" : System.out.println(ANSI_CYAN + "[DISCORD BOT NAME]: " + ANSI_RED + "[ERROR]: " + message + ANSI_RESET); break;
            case "message" : System.out.println(ANSI_CYAN + "[DISCORD BOT NAME]: " + ANSI_PURPLE + "[MESSAGE]: " + ANSI_WHITE + message + ANSI_RESET); break;
            case "success" : System.out.println(ANSI_CYAN + "[DISCORD BOT NAME]: " + ANSI_GREEN + "[SUCCESS]: " + message + ANSI_RESET); break;
        }
    }


}
