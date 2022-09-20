package de.allroundjonu.DiscordBot.TicketSystem;

import de.allroundjonu.DiscordBot.TicketSystem.Database.TicketTypes;
import de.allroundjonu.Main;
import de.allroundjonu.Utils.ConfigFile;
import de.allroundjonu.Utils.CustomLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TicketButtonListener extends ListenerAdapter {

    private static HashMap<String, EmbedBuilder> embeds = Main.embedMessages;
    private static ConfigFile settings = Main.settings;

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);

        if (event.getComponentId().equals("ticketSystemMessageUpdate")) ticketSystemMessageUpdate(event);
        if (event.getComponentId().equals("ticketSystemAddType")) ticketSystemAddType(event);
        if (event.getComponentId().equals("createNewTicketType")) createNewTicketType(event);
        if (event.getComponentId().equals("ticketSystemRemoveType")) deleteTicketType(event);
        if (event.getComponentId().equals("moduleSettingsBack")) return;
    }

    private void ticketSystemMessageUpdate(ButtonInteractionEvent event) {
        event.deferEdit().queue();
        boolean ticketSystemEnabled = (boolean) settings.getObject("moduleSettings.TicketSystem.activated");

        if (!ticketSystemEnabled) {
            event.getHook().editOriginalEmbeds(embeds.get("modulDisableEmbed").build()).setComponents().queue();
            return;
        }

        TextChannel ticketChannel = event.getGuild().getTextChannelById((String) settings.getObject("moduleSettings.TicketSystem.ticketMessageChannelID"));
        EmbedBuilder ticketMessage = embeds.get("ticketSystemMessageEmbed");

        if (TicketTypes.ticketTypes() == null || TicketTypes.ticketTypes().isEmpty()) {
            CustomLogger.error("No Ticket Types found!");
            event.getChannel().sendMessage("No Ticket Types found!").queue();
            event.getHook().deleteOriginal().queue();
            return;
        }

        List<String> ticketTypes = TicketTypes.ticketTypes();
        List<SelectOption> ticketOptions = new ArrayList<>();

        for (String ticketType : ticketTypes) {
            String ticketName = TicketTypes.getField(ticketType, "name");
            String description = TicketTypes.getField(ticketType, "description");
            ticketMessage.addField(ticketName, description, false);
            ticketOptions.add(SelectOption.of(ticketName, ticketType).withDescription(description));
        }

        SelectMenu selectMenu = SelectMenu.create("ticketSystemSelectMenu")
                .setPlaceholder("Select a Ticket Type")
                .addOptions(ticketOptions)
                .build();
        ActionRow actionRow = ActionRow.of(selectMenu);

        ticketChannel.sendMessageEmbeds(ticketMessage.build()).setComponents(actionRow).queue();
        event.getHook().editOriginalEmbeds(embeds.get("ticketSystemMessageUpdateEmbed").setColor(Color.GREEN).build()).setComponents().queue();
    }

    private void ticketSystemAddType(ButtonInteractionEvent event) {
        event.deferEdit().queue();
        boolean ticketSystemEnabled = (boolean) settings.getObject("moduleSettings.TicketSystem.activated");

        if (!ticketSystemEnabled) {
            event.getHook().editOriginalEmbeds(embeds.get("modulDisableEmbed").build()).setComponents().queue();
            return;
        }
        EmbedBuilder embedBuilder = embeds.get("ticketSystemAddTypeEmbed");
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/996796306037153813/1020081239098675210/ezgif-4-84b1a70d8c.gif");
        ActionRow actionRow = ActionRow.of(
                Button.of(ButtonStyle.SUCCESS, "createNewTicketType", "Create Ticket Type", Emoji.fromUnicode("✅"))
        );
        event.getHook().editOriginalEmbeds(embedBuilder.build()).setComponents(actionRow).queue();
    }

    private void createNewTicketType(ButtonInteractionEvent event) {

        boolean ticketSystemEnabled = (boolean) settings.getObject("moduleSettings.TicketSystem.activated");
        if (!ticketSystemEnabled) {
            event.getHook().editOriginalEmbeds(embeds.get("modulDisableEmbed").build()).setComponents().queue();
            return;
        }

        TextInput type = TextInput.create("ticketType", "Ticket Type", TextInputStyle.SHORT)
                .setPlaceholder("Unique ID for the Ticket Type z.B. support")
                .setMinLength(4)
                .setMaxLength(20)
                .setRequired(true)
                .build();
        TextInput typeName = TextInput.create("ticketTypeName", "Ticket Type Name", TextInputStyle.SHORT)
                .setPlaceholder("Name of the Ticket Type z.B. Support")
                .setMinLength(4)
                .setMaxLength(20)
                .setRequired(false)
                .build();
        TextInput channelID = TextInput.create("ticketChannelID", "Ticket Channel ID", TextInputStyle.SHORT)
                .setPlaceholder("Channel ID with the Ticket Settings")
                .setMinLength(18)
                .setMaxLength(20)
                .setRequired(true)
                .build();
        Modal modal = Modal.create("createNewTicketTypeModal", "Create New Ticket Type")
                .addActionRows(ActionRow.of(type), ActionRow.of(typeName), ActionRow.of(channelID)).build();

        event.getMessage().delete().queue();

        event.replyModal(modal).queue();

    }

    private void deleteTicketType(ButtonInteractionEvent event){
        boolean ticketSystemEnabled = (boolean) settings.getObject("moduleSettings.TicketSystem.activated");

        if (!ticketSystemEnabled) {
            event.getHook().editOriginalEmbeds(embeds.get("modulDisableEmbed").build()).setComponents().queue();
            return;
        }

        event.getMessage().delete().queue();

        TextInput type = TextInput.create("tickettype", "Ticket Type", TextInputStyle.SHORT)
                .setPlaceholder("Unique ID of the Ticket Type")
                .setMinLength(4)
                .setMaxLength(50)
                .setRequired(true)
                .build();

        Modal modal = Modal.create("deleteTicketTypeModal", "Delete Ticket Type")
                .addActionRows(ActionRow.of(type))
                .build();
        event.replyModal(modal).queue();
    }
}
