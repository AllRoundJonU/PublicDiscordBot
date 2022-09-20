package de.allroundjonu.DiscordBot.CommandAPI;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    private String commandName;
    private String description;
    private DefaultMemberPermissions defaultMemberPermissions;
    private boolean guildOnly = false;
    private List<OptionData> options = new ArrayList<>();

    public abstract void execute(SlashCommandInteractionEvent event);

    public void onButtonInteraction(ButtonInteractionEvent event){}

    public Command(String command, String description) {
        this.commandName = command;
        this.description = description;
    }

    public void setDefaultMemberPermissions(DefaultMemberPermissions defaultMemberPermissions) {
        this.defaultMemberPermissions = defaultMemberPermissions;
    }

    public void setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
    }

    public void setOptions(List<OptionData> options) {
        this.options = options;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getDescription() {
        return description;
    }

    public DefaultMemberPermissions getDefaultMemberPermissions() {
        return defaultMemberPermissions;
    }

    public boolean isGuildOnly() {
        return guildOnly;
    }

    public List<OptionData> getOptions() {
        return options;
    }
}