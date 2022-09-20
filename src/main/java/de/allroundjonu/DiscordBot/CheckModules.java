package de.allroundjonu.DiscordBot;

import de.allroundjonu.Main;
import de.allroundjonu.Utils.ConfigFile;
import de.allroundjonu.Utils.CustomLogger;
import de.allroundjonu.Utils.SQL.SetupDatabaseTables;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

public class CheckModules {

    public static JDA jda = DiscordBot.JDA;
    public static ConfigFile settings = Main.settings;
    static Guild homeGuild = jda.getGuildById((String) settings.getObject("botSettings.guildID"));

    public static String checkTicketSystem(){

        boolean systemActivated = (boolean) settings.getObject("moduleSettings.TicketSystem.activated");

        if (!systemActivated){
            return "deactivated";
        }

        String messageChannelID = (String) settings.getObject("moduleSettings.TicketSystem.ticketMessageChannelID");
        String ticketLogChannelID = (String) settings.getObject("moduleSettings.TicketSystem.ticketLogChannelID");
        String ticketCategoryID = (String) settings.getObject("moduleSettings.TicketSystem.ticketCategory");

        if (homeGuild.getTextChannelById(messageChannelID) == null){
            CustomLogger.error("Ein Channel mit der ID: " + messageChannelID + " existiert auf dem Server " + homeGuild.getName() + " nicht");
            return "stop";
        }
        if (homeGuild.getTextChannelById(ticketLogChannelID) == null){
            CustomLogger.error("Ein Channel mit der ID: " + ticketLogChannelID + " existiert auf dem Server " + homeGuild.getName() + " nicht");
            return "stop";
        }
        if (homeGuild.getCategoryById(ticketCategoryID) == null){
            CustomLogger.error("Eine Category mit der ID: " + ticketCategoryID + " existiert auf dem Server " + homeGuild.getName() + " nicht");
            return "stop";
        }
        /*if (!SetupDatabaseTables.tableExists("ticket_types", "type")){
            SetupDatabaseTables.createTicketTables();
        }*/
        return "check";
    }

    public static String checkInviteLogger(){

        boolean systemActivated = (boolean) settings.getObject("moduleSettings.InviteLogger.activated");
        if (!systemActivated){
            return "deactivated";
        }

        String inviteLogChannelID = (String) settings.getObject("moduleSettings.InviteLogger.inviteLogChannelID");

        if(homeGuild.getTextChannelById(inviteLogChannelID) == null){
            CustomLogger.error("Ein Channel mit der ID: " + inviteLogChannelID + " existiert auf dem Server " + homeGuild.getName() + " nicht");
            return "stop";
        }
        /*if (!SetupDatabaseTables.tableExists("ticket_types", "type")){
            SetupDatabaseTables.createTicketTables();
        }*/
        return "check";
    }

    public static String checkWishSystem(){

            boolean systemActivated = (boolean) settings.getObject("moduleSettings.WishSystem.activated");
            if (!systemActivated){
                return "deactivated";
            }

            String wishChannelID = (String) settings.getObject("moduleSettings.WishSystem.wishChannelID");
            String wishMessageChannelID = (String) settings.getObject("moduleSettings.WishSystem.wishMessageChannelID");

            if(homeGuild.getCategoryById(wishChannelID) == null){
                CustomLogger.error("Ein Channel mit der ID: " + wishChannelID + " existiert auf dem Server " + homeGuild.getName() + " nicht");
                return "stop";
            }
            if(homeGuild.getCategoryById(wishMessageChannelID) == null){
                CustomLogger.error("Ein Channel mit der ID: " + wishMessageChannelID + " existiert auf dem Server " + homeGuild.getName() + " nicht");
                return "stop";
            }
            /*if (!SetupDatabaseTables.tableExists("ticket_types", "type")){
                SetupDatabaseTables.createTicketTables();
            }*/
            return "check";
    }

}
