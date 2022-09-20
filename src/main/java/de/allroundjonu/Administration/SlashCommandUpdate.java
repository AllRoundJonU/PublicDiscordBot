package de.allroundjonu.Administration;

import de.allroundjonu.DiscordBot.DiscordBot;
import de.allroundjonu.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.ApplicationCommandUpdatePrivilegesEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.privileges.IntegrationPrivilege;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SlashCommandUpdate extends ListenerAdapter {

    @Override
    public void onApplicationCommandUpdatePrivileges(@NotNull ApplicationCommandUpdatePrivilegesEvent event) {
        super.onApplicationCommandUpdatePrivileges(event);

        Guild guild = event.getGuild();
        String commandID = event.getCommandId();
        List<IntegrationPrivilege> privileges = event.getPrivileges();


    }
}
