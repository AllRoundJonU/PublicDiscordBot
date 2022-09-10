package de.allroundjonu.Utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class BotJSONParser {

    private static String settingsFile = "settings.json";
    private String embedsFile = "embeds.json";
    private String localesFile = "locales.json";

    private static JSONObject settings;

    private FileReader settingsReader;

    public static boolean checkSettingFiles(){
        File file = new File("settings.json");
        if (!file.exists() && !file.isDirectory()){
            CustomLogger.log("error", "settings.json existiert nicht");
            return false;
        }
        CustomLogger.log("success", file.getName() + " existiert");
        file = new File("embeds.json");
        if (!file.exists() && !file.isDirectory()){
            CustomLogger.log("error", "embeds.json existiert nicht");
            return false;
        }
        CustomLogger.log("success", file.getName() + " existiert");
        file = new File("locales.json");
        if (!file.exists() && !file.isDirectory()){
            CustomLogger.log("error", "locales.json existiert nicht");
            return false;
        }
        CustomLogger.log("success", file.getName() + " existiert");
        CustomLogger.log("success", "Alle Datein existiert");
        return true;
    }


    public static boolean startupJSONParser(){

        try (FileReader reader1 = new FileReader(settingsFile)){
            settings = (JSONObject) new JSONParser().parse(reader1);
            CustomLogger.log("success", "settings.json is parsed");
            return true;
        } catch (FileNotFoundException e) {
            CustomLogger.log("error", e.getMessage());
            return false;
        } catch (IOException e) {
            CustomLogger.log("error", e.getMessage());
            return false;
        } catch (ParseException e) {
            CustomLogger.log("error", e.getMessage());
            return false;
        }
    }

}
