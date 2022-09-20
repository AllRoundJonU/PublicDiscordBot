package de.allroundjonu.DiscordBot.TicketSystem.Database;

import de.allroundjonu.Utils.CustomLogger;
import de.allroundjonu.Utils.SQL.MySQL;
import net.dv8tion.jda.api.entities.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketTypes {

    public static Connection connection = MySQL.database;

    public static boolean isTicketType(String type){
        MySQL.databaseLastConnection = OffsetDateTime.now();
        String sql = "SELECT * FROM ticket_types WHERE type = '" + type + "'";

        if(!MySQL.isConnected()){
            CustomLogger.error("MySQL is not connected!");
            return false;
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            return resultSet.next();

        } catch (SQLException e) {
            CustomLogger.error(e.getMessage());
            return false;
            //throw new RuntimeException(e);
        }
    }


    public static boolean createTicketType(String type, String name, String description, String channelName, ArrayList<Role> allowedRoles){
        MySQL.databaseLastConnection = OffsetDateTime.now();

        String databaseName = type + "_tickets";

        System.out.println(databaseName);

        String createType = "INSERT INTO `ticket_types`(`type`, `name`, `description`, `channel_name`, `allowed_roles`) " +
                "VALUES ('" + type + "','" + name + "','" + description + "','" + channelName + "' ,'" + allowedRoles + "')";


        String createTicketTable = "CREATE TABLE `" + databaseName + "` (`ticket_nr` int(11) NOT NULL COMMENT 'AUTO INCREMENT', `user_id` varchar(20) NOT NULL, `channel_id` varchar(20) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";
        String modifyTicketTable1 = "ALTER TABLE `" + databaseName + "` ADD PRIMARY KEY (`ticket_nr`);";
        String modifyTicketTable2 = "ALTER TABLE `" + databaseName + "` MODIFY `ticket_nr` int(11) NOT NULL AUTO_INCREMENT;";

        if(!MySQL.isConnected()){
            CustomLogger.error("MySQL is not connected!");
            return false;

        }

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(createType);
            statement.executeUpdate(createTicketTable);
            statement.executeUpdate(modifyTicketTable1);
            statement.executeUpdate(modifyTicketTable2);
            statement.close();
            return true;
        } catch (SQLException e) {
            CustomLogger.error(e.getMessage());
            return false;
        }
    }

    public static void deleteTicketType(String ticketType) {
        MySQL.databaseLastConnection = OffsetDateTime.now();
        String sql = "DELETE FROM `ticket_types` WHERE `type` LIKE '" + ticketType + "'";
        String delete = "DROP TABLE `" + ticketType + "_tickets`";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.executeUpdate(delete);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String getField(String type, String targetField){
        MySQL.databaseLastConnection = OffsetDateTime.now();
        String sql = "SELECT " + targetField + " FROM ticket_types WHERE type = '" + type + "'";

        if(!MySQL.isConnected()){
            CustomLogger.error("MySQL is not connected!");
            return null;
        }

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                return resultSet.getString(targetField);
            }else{
                return null;
            }

        } catch (SQLException e) {
            CustomLogger.error(e.getMessage());
            return null;
            //throw new RuntimeException(e);
        }
    }

    public static List<String> ticketTypes() {
        MySQL.databaseLastConnection = OffsetDateTime.now();
        List<String> ticketTypes = new ArrayList<>();
        String sql = "SELECT * FROM `ticket_types`";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ticketTypes.add(resultSet.getString("type"));
            }
            return ticketTypes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateTicketTypeEmbed(String type, String embedTitle, String embedDescription, String embedFooter) {
        MySQL.databaseLastConnection = OffsetDateTime.now();
        String sql = "UPDATE `ticket_types` SET `embed_title`='" + embedTitle + "',`embed_description`='" + embedDescription + "',`embed_footer`='" + embedFooter + "' WHERE `type` LIKE '" + type + "'";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
