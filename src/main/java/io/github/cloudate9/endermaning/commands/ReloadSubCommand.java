package io.github.cloudate9.endermaning.commands;

import io.github.cloudate9.endermaning.config.MessagesConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class ReloadSubCommand implements SubCommand {

    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;

    @Inject
    public ReloadSubCommand(MessagesConfig messagesConfig, MiniMessage miniMessage) {
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {

        //This command exists solely to make a user feel like they did something. It doesn't do much.
        sender.sendMessage(
                miniMessage.deserialize(messagesConfig.success)
        );
    }

    @Override
    public List<String> getName() {
        return List.of("reload", "rl");
    }
}
