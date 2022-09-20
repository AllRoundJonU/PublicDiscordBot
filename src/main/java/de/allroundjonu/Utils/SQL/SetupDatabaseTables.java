package de.allroundjonu.Utils.SQL;

import de.allroundjonu.Utils.CustomLogger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupDatabaseTables {

    static Connection connection = MySQL.database;

    public static boolean tableExists(String tableName, String fieldName){
        String sql = "SELECT `" + fieldName + "` FROM `" + tableName + "`";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();
        } catch (SQLException e) {
            //CustomLogger.error(e.getMessage());
            return false;
        }

    }

    public static void createTicketTables(){
        String createTicketTypesDatabase = "CREATE TABLE IF NOT EXISTS`ticket_types` (`type` varchar(50) NOT NULL, `name` varchar(50) NOT NULL, `description` longtext NOT NULL DEFAULT 'Bitte öffne ein Ticket um mehr zu erfahren.', `channel_name` varchar(100) NOT NULL DEFAULT 'Ticket', `embed_title` longtext NOT NULL DEFAULT 'Ticket', `embed_content` longtext NOT NULL, `allowed_roles` varchar(5000) DEFAULT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
        String setupTicketTypesDatabase = "ALTER TABLE `ticket_types` ADD UNIQUE KEY (`type`);";

        String createTicketsDatabase = "CREATE TABLE IF NOT EXISTS `tickets` (`user_id` varchar(40) NOT NULL, `channel_id` varchar(40) DEFAULT NULL, `type` varchar(40) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        String setupTicketsDatabase = "ALTER TABLE `tickets` ADD UNIQUE KEY `channel_id` (`channel_id`);";

        String createAdditionalUsersDatabase = "CREATE TABLE IF NOT EXISTS `additional_user` (`ID` int(11) NOT NULL, `user_id` varchar(40) NOT NULL, `channel_id` varchar(40) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
        String setupAdditionalUsersDatabase = "ALTER TABLE `additional_user` ADD PRIMARY KEY (`ID`);";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createTicketTypesDatabase);
            statement.executeUpdate(setupTicketTypesDatabase);

            statement.executeUpdate(createTicketsDatabase);
            statement.executeUpdate(setupTicketsDatabase);

            statement.executeUpdate(createAdditionalUsersDatabase);
            statement.executeUpdate(setupAdditionalUsersDatabase);
            statement.close();
            CustomLogger.message("Datenbank erstellt");
        } catch (SQLException e) {
            e.printStackTrace();
            CustomLogger.error(e.getMessage());
            return;
        }
    }

}
