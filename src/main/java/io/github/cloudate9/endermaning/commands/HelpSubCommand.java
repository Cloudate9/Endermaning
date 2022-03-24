package io.github.cloudate9.endermaning.commands;

import io.github.cloudate9.endermaning.config.MessagesConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class HelpSubCommand implements SubCommand {

    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;

    @Inject
    public HelpSubCommand(MessagesConfig messagesConfig, MiniMessage miniMessage) {
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {

        final TextComponent.Builder messageBuilder = Component.text();
        for (String message : messagesConfig.getAllHelpMessages()) {
            messageBuilder
                    .append(miniMessage.deserialize(message))
                    .append(Component.text("\n"));
        }

        sender.sendMessage(messageBuilder);
    }

    @Override
    public List<String> getName() {
        return List.of("help", "h");
    }
}
