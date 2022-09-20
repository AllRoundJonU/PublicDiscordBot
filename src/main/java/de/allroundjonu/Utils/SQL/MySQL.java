package de.allroundjonu.Utils.SQL;

import de.allroundjonu.Main;
import de.allroundjonu.Utils.ConfigFile;
import de.allroundjonu.Utils.CustomLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class MySQL {

    public static Connection database;

    public static Connection secondDatabase;
    public static OffsetDateTime databaseLastConnection;
    public static OffsetDateTime secondDatabaseLastConnection;

    public static ConfigFile settings = Main.settings;

    public static void connect(){

        String databaseURL = (String) settings.getObject("databaseSettings.botDatabase.url");
        String databaseUser = (String) settings.getObject("databaseSettings.botDatabase.user");
        String databasePassword = (String) settings.getObject("databaseSettings.botDatabase.password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException ex){
            CustomLogger.error(ex.getMessage());
            return;
        }

        if (!isConnected()){
            try {
                database = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
                databaseLastConnection = OffsetDateTime.now();

                Thread databaseThread = new Thread(() -> {
                    while (true) {
                        if(OffsetDateTime.now().minusHours(6).isAfter(databaseLastConnection)){
                            MySQL.updateWithoutException("CREATE TABLE IF NOT EXISTS LebensZeichen(Lebenszeichen VARCHAR(50), PRIMARY KEY (Lebenszeichen))");
                            MySQL.updateWithoutException("DROP TABLE IF EXISTS LebensZeichen");
                        }
                        try {
                            Thread.sleep(1000 * 60 * 60);
                        } catch (InterruptedException e) {
                            CustomLogger.error(e.getMessage());
                        }
                    }
                });
            } catch (SQLException e) {
                CustomLogger.error(e.getMessage());
                //throw new RuntimeException(e);
            }
        }
    }

    public static boolean isConnected() {
        return (database != null);
    }

    public static void updateWithoutException(String qry) {
        if (isConnected()) {
            try {
                database.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            databaseLastConnection = OffsetDateTime.now();
        }
    }




}
