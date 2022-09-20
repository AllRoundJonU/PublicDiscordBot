package de.allroundjonu.DiscordBot.TicketSystem;

import de.allroundjonu.DiscordBot.TicketSystem.Database.TicketTypes;
import de.allroundjonu.Main;
import de.allroundjonu.Utils.SQL.MySQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TicketModalListener extends ListenerAdapter {

    private HashMap<String, EmbedBuilder> embeds = Main.embedMessages;

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        super.onModalInteraction(event);

        if (event.getModalId().equals("createNewTicketTypeModal")) createNewTicketTypeSettings(event);
        if (event.getModalId().equals("deleteTicketTypeModal")) deleteTicketTypeSettings(event);

    }

    private void createNewTicketTypeSettings(ModalInteractionEvent event){
        String ticketType = event.getValue("ticketType").getAsString();
        String channelID = event.getValue("ticketChannelID").getAsString();
        String ticketName = event.getValue("ticketTypeName").getAsString();


        if (TicketTypes.isTicketType(ticketType)) {
            event.getHook().editOriginalEmbeds(embeds.get("ticketTypeAlreadyExistsEmbed").build()).queue();
            return;
        }

        if (TicketTypes.ticketTypes().size() >= 15) {
            event.getHook().editOriginalEmbeds(embeds.get("ticketTypeLimitReachedEmbed").build()).queue();
            return;
        }

        TextChannel textChannel = event.getGuild().getTextChannelById(channelID);
        if (textChannel == null){
            event.reply("The channel you selected does not exist!").setEphemeral(true).queue();
            return;
        }
        String channelName = textChannel.getName();
        String ticketDescription = textChannel.getTopic();
        ArrayList<Role> allowedRoles = new ArrayList<>();

        if (ticketName == null || ticketName.isEmpty()) {
            ticketName = channelName;
        }

        List<PermissionOverride> channelRoles = textChannel.getRolePermissionOverrides();
        for (PermissionOverride permissionOverride : channelRoles) {
            Role role = permissionOverride.getRole();
            if (role.hasPermission(textChannel, Permission.VIEW_CHANNEL)) allowedRoles.add(role);
        }
        StringBuilder allowedRolesString = new StringBuilder();
        for (Role role : allowedRoles) {
            allowedRolesString.append(role.getAsMention()).append(" ");
        }

        EmbedBuilder embedBuilder = embeds.get("ticketSystemCreateNewTicketType");
        embedBuilder.addField("Ticket Type", ticketType, true);
        embedBuilder.addField("Ticket Name", ticketName, true);

        if (ticketDescription != null) {
            embedBuilder.addField("Ticket Description", ticketDescription, false);
        } else {
            ticketDescription = "No description";
            embedBuilder.addField("Ticket Description", "No description set", false);
        }

        if (allowedRolesString.length() > 0) {
            embedBuilder.addField("Allowed Roles", allowedRolesString.toString(), false);
        } else {
            embedBuilder.addField("Allowed Roles", "No roles set", false);
        }

        ActionRow actionRow = ActionRow.of(
                Button.of(ButtonStyle.PRIMARY, "createTicketTypeEmbedMessage", "Add Message", Emoji.fromUnicode("\uD83D\uDCDC"))
        );

        TicketTypes.createTicketType(ticketType, ticketName, ticketDescription, channelName, allowedRoles);
        textChannel.delete().queue();

        event.replyEmbeds(embedBuilder.build()).setComponents(actionRow).queue();

    }
    private void deleteTicketTypeSettings(ModalInteractionEvent event){
        String ticketType = event.getValue("ticketType").getAsString();

        if (!TicketTypes.isTicketType(ticketType)) {
            event.getHook().editOriginalEmbeds(embeds.get("ticketTypeDoesNotExistEmbed").build()).queue();
            return;
        }
        TicketTypes.deleteTicketType(ticketType);
        EmbedBuilder embedBuilder = embeds.get("ticketSystemDeleteTicketType");

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
