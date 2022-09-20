package de.allroundjonu.DiscordBot.CommandAPI;

import de.allroundjonu.Administration.AdministrationCommand;
import de.allroundjonu.DiscordBot.DiscordBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private final ArrayList<Command> commands = new ArrayList<>();

    public CommandManager() {
        registerCommand(new AdministrationCommand());

    }

    public void registerCommand(Command command){
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }


    public void registerCommands(){
        JDA jda = DiscordBot.JDA;

        List<Guild> guilds = jda.getGuildCache().stream().toList();

        Collection<CommandData> commandData = new ArrayList<>();

        for(Command command: commands){
            if(command.getDefaultMemberPermissions() != null) {
                commandData.add(Commands.slash(command.getCommandName(),
                                command.getDescription()).
                        setGuildOnly(command.isGuildOnly()).setDefaultPermissions(command.getDefaultMemberPermissions()).
                        addOptions(command.getOptions()));
               /* guild.updateCommands().addCommands(Commands.slash(command.getCommandName(),
                        command.getDescription()).
                        setGuildOnly(command.isGuildOnly()).setDefaultPermissions(command.getDefaultMemberPermissions()).
                        addOptions(command.getOptions())).complete();*/

            }else{
                commandData.add(Commands.slash(command.getCommandName(),
                                command.getDescription()).
                        setGuildOnly(command.isGuildOnly()).addOptions(command.getOptions()));
               /* guild.updateCommands().addCommands(Commands.slash(command.getCommandName(),
                        command.getDescription()).
                        setGuildOnly(command.isGuildOnly()).addOptions(command.getOptions())).complete();*/
            }
        }
        for (Guild guild : guilds){
            guild.updateCommands().addCommands(commandData).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        for(Command c:commands){
            if(c.getCommandName().equalsIgnoreCase(event.getName())){
                c.execute(event);
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for(Command c: commands){
            c.onButtonInteraction(event);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        registerCommands();
    }

}
