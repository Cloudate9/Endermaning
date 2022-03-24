package io.github.cloudate9.endermaning.commands;

import io.github.cloudate9.endermaning.config.MessagesConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@SuppressWarnings("ClassCanBeRecord")
public class SubCommandManager implements CommandExecutor, TabCompleter {

    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final Set<SubCommand> subCommands;
    private final SubCommand defaultConsoleSubCommand;
    private final SubCommand defaultPlayerSubCommand;


    @Inject
    public SubCommandManager(
            MessagesConfig messagesConfig,
            MiniMessage miniMessage,
            Set<SubCommand> subCommands,
            @Named("defaultConsole") SubCommand defaultConsoleSubCommand,
            @Named("defaultPlayer") SubCommand defaultPlayerSubCommand
    ) {
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
        this.subCommands = subCommands;
        this.defaultConsoleSubCommand = defaultConsoleSubCommand;
        this.defaultPlayerSubCommand = defaultPlayerSubCommand;
    }


    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        if (sender instanceof Player && !sender.hasPermission("endermaning.configure")) {
            sender.sendMessage(

                    miniMessage.deserialize(messagesConfig.noPerms)
            );
            return true;
        }

        List<String> subArgs = Arrays.asList(args);

        if (args.length > 0) {
            //Give a view instead of editing the list. Editing gives an error.
            subArgs = subArgs.subList(1, subArgs.size());

            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().contains(args[0].toLowerCase())) {
                    subCommand.call(sender, subArgs);
                    return true;
                }
            }
        }

        if (sender instanceof Player) defaultPlayerSubCommand.call(sender, subArgs);
        else defaultConsoleSubCommand.call(sender, subArgs);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull Command command,
            @NotNull String alias,
            @NotNull String[] args
    ) {
        //Perhaps this implementation isn't the most efficient...

        //Always work with args.length - 1, as the latest arg is the command that is being typed.
        if (args.length == 1) {
            List<String> tabCompleteList = new ArrayList<>();

            for (SubCommand subCommand : subCommands)
                tabCompleteList.addAll(subCommand.getTabComplete().get(0).get(List.of()));
            return StringUtil.copyPartialMatches(args[0], tabCompleteList, new ArrayList<>());
        }

        for (SubCommand subCommand : subCommands) {
            if (subCommand.getTabComplete().size() < args.length) continue;

            for (int i = 1; i < args.length; ++i) { //Start with i = 1, because i = 0 has no requirements.

                boolean found = false;

                //Check if prior subcommand is valid.
                for (Map.Entry<List<String>, List<String>> entry : subCommand.getTabComplete().get(i).entrySet()) {
                    //Key should contain past argument.
                    if (entry.getKey().contains(args[i - 1].toLowerCase())) {

                        if (i + 1 == args.length)
                            return StringUtil.copyPartialMatches(args[i], entry.getValue(), new ArrayList<>());
                        else { //Unnecessary else, but just here for clarity.
                            found = true;
                            break;
                        }

                    }
                }

                if (!found) break;
            }
        }
        return List.of();
    }
}
