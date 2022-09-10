package de.allroundjonu;

import de.allroundjonu.Checking.Startup;
import de.allroundjonu.Utils.BotJSONParser;
import de.allroundjonu.Utils.ConfigFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {


        // Der Bot muss alle Settings checken und validieren!!
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

    }
}