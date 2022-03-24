package io.github.cloudate9.endermaning.commands;

import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public class ListSubCommand implements SubCommand {

    private final HybridConfig hybridConfig;
    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;

    @Inject
    public ListSubCommand(HybridConfig hybridConfig, MessagesConfig messagesConfig, MiniMessage miniMessage) {
        this.hybridConfig = hybridConfig;
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {

        if (hybridConfig.hybridList.isEmpty()) sender.sendMessage(
                miniMessage.deserialize(messagesConfig.noHybridsFound)
        );

        else for (int i = 0; i < hybridConfig.hybridList.size(); ++i) {
            sender.sendMessage(
                    Component.text( //Use 1 indexed instead of 0 indexed listing.
                            (i + 1) + " " + Bukkit.getOfflinePlayer(
                                    UUID.fromString(hybridConfig.hybridList.get(i))
                            ).getName()
                    )
            );
        }

    }

    @Override
    public List<String> getName() {
        return List.of("list", "ls");
    }
}
