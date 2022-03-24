package io.github.cloudate9.endermaning.commands;

import io.github.cloudate9.endermaning.config.MessagesConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;

public class GuiSubCommand implements SubCommand {

    private final MiniMessage miniMessage;
    private final MessagesConfig messagesConfig;

    @Inject
    public GuiSubCommand(MiniMessage miniMessage, MessagesConfig messagesConfig) {
        this.miniMessage = miniMessage;
        this.messagesConfig = messagesConfig;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.noConsoleUse)
            );
            return;
        }

        if (!sender.hasPermission("endermaning.configure")) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.noPerms)
            );
            return;
        }

        //TODO GUI
        sender.sendMessage(
                Component.text(
                        "Seems like you stumbled onto the gui! It's currently isn't ready yet though. " +
                                "Try using /edm help to get cli instructions for now!"
                )
        );
    }


    @Override
    public List<String> getName() {
        return List.of("gui");
    }
}
