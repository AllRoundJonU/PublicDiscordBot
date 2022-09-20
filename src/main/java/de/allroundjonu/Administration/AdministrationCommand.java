package de.allroundjonu.Administration;

import de.allroundjonu.DiscordBot.CommandAPI.Command;
import de.allroundjonu.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.awt.*;
import java.util.HashMap;

public class AdministrationCommand extends Command {

    public static HashMap<String, EmbedBuilder> embedMessages = Main.embedMessages;


    public AdministrationCommand() {
        super("administration", "Administration command");
        setDefaultMemberPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        EmbedBuilder adminEmbedBuilder = embedMessages.get("administrationEmbed");
        adminEmbedBuilder.setColor(new Color(17, 0, 255));

        ActionRow actionRow = ActionRow.of(
                SelectMenu.create("administrationEmbedSelectMenu")
                        .setPlaceholder("Modul Settings")
                        .addOption("Ticket System", "ticketSystem", "Ticket System", Emoji.fromUnicode("🎫"))
                        .addOption("Wish System", "wishSystem", "Wish System", Emoji.fromUnicode("\uD83C\uDF20"))
                        .addOption("Invite Logger", "inviteLogger", "Invite Logger", Emoji.fromUnicode("\uD83D\uDD17"))
                        .addOption("Auto Role", "autoRole", "Auto Role", Emoji.fromUnicode("\uD83D\uDC65"))
                        .addOption("Welcome Message", "welcomeMessage", "Welcome Message", Emoji.fromUnicode("\uD83D\uDC4B"))
                        .addOption("Leave Message", "leaveMessage", "Leave Message", Emoji.fromUnicode("\uD83D\uDC4E"))
                        .setMaxValues(1)
                        .setMinValues(1)
                        .build()
                );
        event.getHook().sendMessageEmbeds(adminEmbedBuilder.build()).addComponents(actionRow).queue();
    }
}
