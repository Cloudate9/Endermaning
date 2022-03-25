package io.github.cloudate9.endermaning.commands;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import io.github.cloudate9.endermaning.hider.HideNormals;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class HidePumpkinPlayersSubCommand implements SubCommand {

    private final HybridConfig hybridConfig;
    private final JavaPlugin plugin;
    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final OptionsConfig optionsConfig;

    @Inject
    public HidePumpkinPlayersSubCommand(
            HybridConfig hybridConfig,
            JavaPlugin plugin,
            MessagesConfig messagesConfig,
            MiniMessage miniMessage,
            OptionsConfig optionsConfig
    ) {
        this.hybridConfig = hybridConfig;
        this.plugin = plugin;
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
        this.optionsConfig = optionsConfig;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.hidePumpkinPlayersHelp)
            );
            return;
        }

        switch (args.get(0).toLowerCase()) {
            case "enable", "true" -> {
                optionsConfig.hidePumpkinPlayersFromHybrids = true;
                HideNormals.hideAllNormals(hybridConfig.hybridList, plugin);
            }

            case "disable", "false" -> {
                optionsConfig.hidePumpkinPlayersFromHybrids = false;

                HideNormals.showAllNormals(hybridConfig.hybridList, plugin);
            }

            default -> {
                sender.sendMessage(
                        miniMessage.deserialize(messagesConfig.hidePumpkinPlayersHelp)
                );
                return;
            }
        }

        Config.saveConfig(optionsConfig);
        sender.sendMessage(
                miniMessage.deserialize(messagesConfig.success)
        );
    }

    @Override
    public List<String> getName() {
        return List.of("hidepumpkinplayers", "hidep", "hpp");
    }

    @Override
    public List<Map<List<String>, List<String>>> getTabComplete() {
        return List.of(
                Map.of(List.of(), getName()),
                Map.of(getName(), List.of("enable", "disable"))
        );
    }
}
