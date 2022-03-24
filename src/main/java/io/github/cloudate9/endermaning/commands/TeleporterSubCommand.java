package io.github.cloudate9.endermaning.commands;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import io.github.cloudate9.endermaning.teleporter.TeleporterObject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class TeleporterSubCommand implements SubCommand {

    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final OptionsConfig optionsConfig;
    private final TeleporterObject teleporterObject;

    @Inject
    public TeleporterSubCommand(
            MessagesConfig messagesConfig,
            MiniMessage miniMessage,
            OptionsConfig optionsConfig,
            TeleporterObject teleporterObject
    ) {
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
        this.optionsConfig = optionsConfig;
        this.teleporterObject = teleporterObject;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.teleporterHelp)
            );
            return;
        }

        switch (args.get(0).toLowerCase()) {
            case "enable", "true" -> optionsConfig.teleporterEnabled = true;
            case "disable", "false" -> optionsConfig.teleporterEnabled = false;
            case "random" -> optionsConfig.randomInsteadOfInfiniteTeleporter = true;
            case "infinite" -> optionsConfig.randomInsteadOfInfiniteTeleporter = false;
            default -> {
                sender.sendMessage(
                        miniMessage.deserialize(messagesConfig.teleporterHelp)
                );
                return;
            }
        }

        /*
        Only online players' teleporters need to be managed.
        Offline players will automatically have their teleporters updated when they log on.
         */

        switch (args.get(0).toLowerCase()) {
            case "disable", "false" -> {
                for (Player player : Bukkit.getOnlinePlayers()) teleporterObject.removeIfPresent(player);
            }

            default -> {
                for (Player player : Bukkit.getOnlinePlayers()) teleporterObject.giveIfAbsent(player);
            }
        }

        Config.saveConfig(optionsConfig);
        sender.sendMessage(
                miniMessage.deserialize(messagesConfig.success)
        );
    }

    @Override
    public List<String> getName() {
        return List.of("teleporter", "tp");
    }

    @Override
    public List<Map<List<String>, List<String>>> getTabComplete() {
        return List.of(
                Map.of(List.of(), getName()),
                Map.of(
                        getName(), List.of("enable", "disable", "random", "infinite")
                )
        );
    }
}
