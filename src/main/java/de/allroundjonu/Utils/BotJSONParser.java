package de.allroundjonu.Utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class BotJSONParser {

    private static String settingsFile = "settings.json";
    private static String embedsFile = "embeds.json";
    private static String localesFile = "locales.json";

    public static boolean checkSettingFiles(){
        File file = new File("settings.json");
        if (!file.exists() && !file.isDirectory()){
            CustomLogger.error("settings.json existiert nicht");
            return false;
        }
        CustomLogger.success(file.getName() + " existiert");
        file = new File("embeds.json");
        if (!file.exists() && !file.isDirectory()){
            CustomLogger.error("embeds.json existiert nicht");
            return false;
        }
        CustomLogger.success(file.getName() + " existiert");
        file = new File("locales.json");
        if (!file.exists() && !file.isDirectory()){
            CustomLogger.error("locales.json existiert nicht");
            return false;
        }
        CustomLogger.success(file.getName() + " existiert");
        CustomLogger.success("Alle Datein existiert");
        return true;
    }


    public static ConfigFile readConfig(Config config){
        try{
            ConfigFile settings = (ConfigFile) new JSONParser().parse(new FileReader(config.getPath()));
            CustomLogger.success("settings.json is parsed");
            return settings;
        } catch (IOException | ParseException e) {
            CustomLogger.error(e.getMessage());
            return null;
        }
    }


    public enum Config{
        SETTINGS(settingsFile), EMBEDS(embedsFile), LOCALS(localesFile);

        String path;
        Config(String path){
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

}
