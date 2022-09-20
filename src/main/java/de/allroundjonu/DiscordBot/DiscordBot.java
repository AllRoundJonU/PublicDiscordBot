package de.allroundjonu.DiscordBot;

import de.allroundjonu.Administration.AdministrationSelectMenus;
import de.allroundjonu.DiscordBot.CommandAPI.CommandManager;
import de.allroundjonu.DiscordBot.TicketSystem.TicketButtonListener;
import de.allroundjonu.DiscordBot.TicketSystem.TicketModalListener;
import de.allroundjonu.Main;
import de.allroundjonu.Utils.ConfigFile;
import de.allroundjonu.Utils.CustomLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class DiscordBot {

    public static JDA JDA;
    public static JDABuilder jdaBuilder;
    public static ConfigFile settings = Main.settings;

    private static CommandManager commandManager;

    public static void stopDiscordBot(){
        JDA.shutdownNow();
        CustomLogger.message("Discord-Bot wird gestoppt");
    }

    public static void startDiscordBot() {

        String toke = (String) settings.getObject("botSettings.botToke");
        String homeGuild = (String) settings.getObject("botSettings.guildID");
        String activityType = (String) settings.getObject("botSettings.botActivity.activityType");
        String activityMessage = (String) settings.getObject("botSettings.botActivity.activityMessage");


        try {
            jdaBuilder = JDABuilder.create(toke,
                    GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.GUILD_BANS,
                    GatewayIntent.GUILD_INVITES,
                    GatewayIntent.GUILD_WEBHOOKS,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                    GatewayIntent.GUILD_VOICE_STATES);
            jdaBuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
            jdaBuilder.setChunkingFilter(ChunkingFilter.ALL);
            jdaBuilder.setBulkDeleteSplittingEnabled(false);
            jdaBuilder.setLargeThreshold(250);
            jdaBuilder.setAutoReconnect(true);

            if (activityType.equalsIgnoreCase("playing")){
                jdaBuilder.setActivity(Activity.playing(activityMessage));
            } else if (activityType.equalsIgnoreCase("watching")){
                jdaBuilder.setActivity(Activity.watching(activityMessage));
            } else if (activityType.equalsIgnoreCase("competing")){
                jdaBuilder.setActivity(Activity.competing(activityMessage));
            }

            commandManager = new CommandManager();

            jdaBuilder.addEventListeners(
                    commandManager,
                    new AdministrationSelectMenus(),
                    new TicketButtonListener(),
                    new TicketModalListener());

            JDA = jdaBuilder.build();
            JDA.awaitReady();

            if (JDA.getGuildById(homeGuild) == null){
                JDA.shutdownNow();
                CustomLogger.error("Discord-Bot konnte nicht gestartet werden: Bot nicht auf Home Server");
                return;
            }

            if (isConnected()){
                CustomLogger.success("Bot Online as: " + JDA.getSelfUser().getName());
            }

        } catch (InterruptedException e) {
            CustomLogger.error(e.getMessage());
            //throw new RuntimeException(e);
        }

    }

    public static boolean isConnected() {
        return (JDA != null);
    }

}
