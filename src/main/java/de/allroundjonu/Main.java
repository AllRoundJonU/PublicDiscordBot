package de.allroundjonu;

import de.allroundjonu.DiscordBot.DiscordBot;
import de.allroundjonu.DiscordBot.CheckModules;
import de.allroundjonu.DiscordBot.LoadEmbeds;
import de.allroundjonu.Utils.BotJSONParser;
import de.allroundjonu.Utils.ConfigFile;
import de.allroundjonu.Utils.ConfigValidator;
import de.allroundjonu.Utils.CustomLogger;
import de.allroundjonu.Utils.SQL.MySQL;
import de.allroundjonu.Utils.SQL.SetupDatabaseTables;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static ConfigFile settings;
    public static ConfigFile embeds;
    public static HashMap<String, EmbedBuilder> embedMessages = new HashMap<>();
    private static ConfigValidator configValidator;
    public static void main(String[] args) throws IOException, InterruptedException {


        // Der Bot muss alle Settings checken und validieren!
        /*
        * Bevor der Bot die Settings checkt er, ob alle settings Datein überhaupt existieren
        * In folgender Reihenfolge soll der Bot alles checken:
        * 1. Datenbank Strings
        * 1.1 Sollte es so kommen das Ticket auf GitHub gespeichert werden muss dort der Dev Key auch geprüft werden
        * 2. Bot Token
        * 3. Alle anderen Settings der Reihe nacht
        * 4. Alle Embeds in der noch kommenden embeds.json
        * 5. Alle anderen Language files für z.B. verschiedene outputs des Bots oder der Text der Buttons
        * 
        * Nur wenn der Bot alle einstellungen richtig laden kann bleibt der Bot auch Online
        * sollte es einen fehler geben fährt der Bot sich Automatisch runter
        *
        * Alle Einstellungen werden als Variablen gespeichert so, das später nichts mehr aus den Dateien ausgelesen werden muss.
        * Heißt aber auch änderungen werden nur wirksam, wenn der Bot neu gestartet wird
        * */

        CustomLogger.message("" +
                "\n   _____ _______       _____ _______ _    _ _____  \n" +
                " / ____|__   __|/\\   |  __ \\__   __| |  | |  __ \\ \n" +
                "| (___    | |  /  \\  | |__) | | |  | |  | | |__) |\n" +
                " \\___ \\   | | / /\\ \\ |  _  /  | |  | |  | |  ___/ \n" +
                " ____) |  | |/ ____ \\| | \\ \\  | |  | |__| | |     \n" +
                "|_____/   |_/_/    \\_\\_|  \\_\\ |_|   \\____/|_|     \n" +
                "                                                  \n" +
                "                                                  ");

        CustomLogger.message("Checking if all necessary files exist...");
        if (!BotJSONParser.checkSettingFiles()){
            CustomLogger.error("Bot wird gestoppt!");
            return;
        }
        CustomLogger.message("Parsing .json files...");
        settings = BotJSONParser.readConfig(BotJSONParser.Config.SETTINGS);
        if (settings == null){
            CustomLogger.error("Parsing error!!");
            return;
        }
        embeds = BotJSONParser.readConfig(BotJSONParser.Config.EMBEDS);
        if (embeds == null){
            CustomLogger.error("Parsing error!!");
            return;
        }

        configValidator = new ConfigValidator(settings);
        CustomLogger.message("Checking Database settings...");
        List<String> keys = new ArrayList<>();
        keys.add("databaseSettings.botDatabase.url");
        keys.add("databaseSettings.botDatabase.user");
        keys.add("databaseSettings.botDatabase.password");

        boolean isBotDatabaseSettingsValid = configValidator.doValidationCheck(objects -> {
            MySQL.connect();
            return MySQL.isConnected();
        }, keys);
        keys.clear();

        if (!isBotDatabaseSettingsValid){
            CustomLogger.error("Datenbank verbindung fehlgschlagen");
            return;
        }
        CustomLogger.success("Eine Datenbank verbindung konnte hergestellt werden");
        CustomLogger.message("CHecking Discord-Bot settings...");
        keys.add("botSettings.botToke");
        keys.add("botSettings.guildID");
        keys.add("botSettings.botActivity.activityType");
        keys.add("botSettings.botActivity.activityMessage");

        boolean isDiscordBotSettingsValid = configValidator.doValidationCheck(objects -> {
            DiscordBot.startDiscordBot();
            return DiscordBot.isConnected();
        }, keys);

        if (!isDiscordBotSettingsValid){
            CustomLogger.error("Discord-Bot konnte nicht gestartet werden.");
            return;
        }
        keys.clear();
        CustomLogger.message("Checking Bot modules...");
        keys.add("moduleSettings.TicketSystem.activated");
        keys.add("moduleSettings.TicketSystem.ticketMessageChannelID");
        keys.add("moduleSettings.TicketSystem.ticketCategory");

        boolean isTicketSystemValid = configValidator.doValidationCheck(objects -> {
            String output = CheckModules.checkTicketSystem();
            if (output.equalsIgnoreCase("deactivated")){
                CustomLogger.message("Ticket System deactivated");
                return true;
            } else if (output.equalsIgnoreCase("stop")){
                CustomLogger.error("Ticket System konnte nicht erstellt werden!!");
                return false;
            }else {
                CustomLogger.success("Ticketsystem erfolgreich gestartet.");
                return true;
            }
        }, keys);

        keys.clear();
        if (!isTicketSystemValid){
            DiscordBot.stopDiscordBot();
            return;
        }
      /*  if (!SetupDatabaseTables.tableExists("ticket_types", "type")) {
            CustomLogger.message("botdatabase.ticketypes exestiert nicht!");
            return;
        }*/
        keys.add("moduleSettings.InviteLogger.activated");
        keys.add("moduleSettings.InviteLogger.inviteLogChannelID");
        keys.add("moduleSettings.InviteLogger.countLeave");
        keys.add("moduleSettings.InviteLogger.countBan");
        keys.add("moduleSettings.InviteLogger.countNewAccount");
        boolean isInviteLoggerValid = configValidator.doValidationCheck(objects -> {
               String output = CheckModules.checkInviteLogger();
            if (output.equalsIgnoreCase("deactivated")){
                CustomLogger.message("Invite Logger deactivated");
                return true;
            } else if (output.equalsIgnoreCase("stop")){
                CustomLogger.error("Invite Logger konnte nicht erstellt werden!!");
                return false;
            }else {
                CustomLogger.success("Invite Logger erfolgreich gestartet.");
                return true;
            }
        }, keys);

        keys.clear();
        if (!isInviteLoggerValid){
            DiscordBot.stopDiscordBot();
            return;
        }
        keys.add("moduleSettings.wishSystem.activated");
        keys.add("moduleSettings.wishSystem.wishChannelID");
        keys.add("moduleSettings.wishSystem.wishMessageChannelID");
        boolean isWishSystemValid = configValidator.doValidationCheck(objects -> {
            String output = CheckModules.checkWishSystem();
            if (output.equalsIgnoreCase("deactivated")){
                CustomLogger.message("Wish System deactivated");
                return true;
            } else if (output.equalsIgnoreCase("stop")){
                CustomLogger.error("Wish System konnte nicht erstellt werden!!");
                return false;
            }else {
                CustomLogger.success("Wish System erfolgreich gestartet.");
                return true;
            }
        }, keys);

        LoadEmbeds.loadEmbeds();
    }
}