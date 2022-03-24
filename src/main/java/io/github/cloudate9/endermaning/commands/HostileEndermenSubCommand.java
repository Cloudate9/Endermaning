package io.github.cloudate9.endermaning.commands;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class HostileEndermenSubCommand implements SubCommand {

    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final OptionsConfig optionsConfig;

    @Inject
    public HostileEndermenSubCommand(
            MessagesConfig messagesConfig,
            MiniMessage miniMessage,
            OptionsConfig optionsConfig
    ) {
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
        this.optionsConfig = optionsConfig;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.hostileEndermenHelp)
            );
            return;
        }

        switch (args.get(0).toLowerCase()) {
            case "enable", "true" -> optionsConfig.endermenHostileToHybrids = true;
            case "disable", "false" -> optionsConfig.endermenHostileToHybrids = false;

            default -> {
                sender.sendMessage(
                        miniMessage.deserialize(messagesConfig.hostileEndermenHelp)
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
        return List.of("hostileEndermen", "hostile");
    }

    @Override
    public List<Map<List<String>, List<String>>> getTabComplete() {
        return List.of(
                Map.of(List.of(), getName()),
                Map.of(getName(), List.of("enable", "disable"))
        );
    }
}
