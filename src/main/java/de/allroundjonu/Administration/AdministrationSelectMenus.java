package de.allroundjonu.Administration;

import de.allroundjonu.DiscordBot.TicketSystem.Database.TicketTypes;
import de.allroundjonu.Main;
import de.allroundjonu.Utils.ConfigFile;
import de.allroundjonu.Utils.CustomLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdministrationSelectMenus extends ListenerAdapter {

    public static HashMap<String, EmbedBuilder> embedMessages = Main.embedMessages;
    public static ConfigFile settings = Main.settings;

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        super.onSelectMenuInteraction(event);

        if (!event.getComponentId().equals("administrationEmbedSelectMenu")) return;

        if (event.getValues().get(0).equals("ticketSystem")) ticketSystem(event);
    }

    private void ticketSystem(SelectMenuInteractionEvent event) {
        event.deferEdit().queue();

        boolean systemActivated = (boolean) settings.getObject("moduleSettings.TicketSystem.activated");
        String messageChannelID = (String) settings.getObject("moduleSettings.TicketSystem.ticketMessageChannelID");
        String ticketLogChannelID = (String) settings.getObject("moduleSettings.TicketSystem.ticketLogChannelID");
        String ticketCategoryID = (String) settings.getObject("moduleSettings.TicketSystem.ticketCategory");

        EmbedBuilder ticketSystemEmbedBuilder = embedMessages.get("ticketSystemEmbed");

        if (ticketSystemEmbedBuilder.getFields() != null) ticketSystemEmbedBuilder.clearFields();

        ticketSystemEmbedBuilder.addField("Modul Status", systemActivated ? "Aktiviert" : "Deaktiviert", false);
        ticketSystemEmbedBuilder.addField("Ticket Nachrichten Channel", "<#" + messageChannelID + ">", false);
        ticketSystemEmbedBuilder.addField("Ticket Log Channel", "<#" + ticketLogChannelID + ">", false);
        ticketSystemEmbedBuilder.addField("Ticket Kategorie", "<#" + ticketCategoryID + ">", false);

        ActionRow actionRow = ActionRow.of(
                Button.of(ButtonStyle.PRIMARY, "ticketSystemMessageUpdate", "Update Message", Emoji.fromUnicode("\uD83D\uDD01")),
                Button.of(ButtonStyle.SUCCESS, "ticketSystemAddType", "Add Type", Emoji.fromUnicode("\uD83D\uDCDD")),
                Button.of(ButtonStyle.DANGER, "ticketSystemRemoveType", "Remove Type", Emoji.fromUnicode("\uD83D\uDDD1")),
                Button.of(ButtonStyle.SECONDARY, "moduleSettingsBack", "Back", Emoji.fromUnicode("\uD83D\uDD19"))
        );

        if (TicketTypes.ticketTypes() == null || TicketTypes.ticketTypes().size() == 0){
            event.getHook().editOriginalEmbeds(ticketSystemEmbedBuilder.build()).setComponents(actionRow).queue();
            return;
        }

        List<String> ticketTypes = TicketTypes.ticketTypes();
        List<SelectOption> ticketOptions = new ArrayList<>();

        for (String ticketType : ticketTypes) {
            String ticketName = TicketTypes.getField(ticketType, "name");
            String description = TicketTypes.getField(ticketType, "description");
            ticketOptions.add(SelectOption.of(ticketName, ticketType).withDescription(description));
        }

        SelectMenu selectMenu = SelectMenu.create("ticketSystemSelectMenu")
                .setPlaceholder("List of Ticket Types")
                .addOptions(ticketOptions)
                .build();
        ActionRow actionRow2 = ActionRow.of(selectMenu);

        event.getHook().editOriginalEmbeds(ticketSystemEmbedBuilder.build()).setComponents(actionRow, actionRow2).queue();

    }
}

