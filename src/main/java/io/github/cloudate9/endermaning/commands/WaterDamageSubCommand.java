package io.github.cloudate9.endermaning.commands;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import io.github.cloudate9.endermaning.water.WaterChecker;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class WaterDamageSubCommand implements SubCommand {

    private final HybridConfig hybridConfig;
    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final OptionsConfig optionsConfig;
    private final WaterChecker waterChecker;

    @Inject
    public WaterDamageSubCommand(
            HybridConfig hybridConfig,
            MessagesConfig messagesConfig,
            MiniMessage miniMessage,
            OptionsConfig optionsConfig,
            WaterChecker waterChecker
    ) {
        this.hybridConfig = hybridConfig;
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
        this.optionsConfig = optionsConfig;
        this.waterChecker = waterChecker;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.waterDamageEnabledHelp)
                            .append(Component.text("\n"))
                            .append(miniMessage.deserialize(messagesConfig.waterDamageValueHelp))
            );
            return;
        }

        switch (args.get(0).toLowerCase()) {
            case "enable", "true" -> {
                optionsConfig.waterDamageEnabled = true;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (hybridConfig.hybridList.contains(player.getUniqueId().toString()))
                        waterChecker.addToCheck(player);
                }
            }
            case "disable", "false" -> {
                optionsConfig.waterDamageEnabled = false;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (hybridConfig.hybridList.contains(player.getUniqueId().toString()))
                        waterChecker.removeFromCheck(player);
                }
            }
            case "peaceful", "easy", "normal", "hard" -> {
                if (args.size() < 2) {
                    sender.sendMessage(
                            miniMessage.deserialize(messagesConfig.waterDamageValueHelp)
                    );
                    return;
                }

                int damagePerTick;

                try {
                    damagePerTick = Integer.parseInt(args.get(1));
                } catch (NumberFormatException ex) {
                    sender.sendMessage(
                            miniMessage.deserialize(messagesConfig.waterDamageValueHelp)
                    );
                    return;
                }

                if (damagePerTick < 0) sender.sendMessage(
                        miniMessage.deserialize(messagesConfig.negativeNumberWarning)
                );

                switch (args.get(0).toLowerCase()) {
                    case "peaceful" -> optionsConfig.waterPeacefulDamage = damagePerTick;
                    case "easy" -> optionsConfig.waterEasyDamage = damagePerTick;
                    case "normal" -> optionsConfig.waterNormalDamage = damagePerTick;
                    case "hard" -> optionsConfig.waterHardDamage = damagePerTick;
                }

            }

            default -> {
                sender.sendMessage(
                        miniMessage.deserialize(messagesConfig.waterDamageEnabledHelp)
                                .append(Component.text("\n"))
                                .append(miniMessage.deserialize(messagesConfig.waterDamageValueHelp))
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
        return List.of("waterdamage", "water", "wd");
    }

    @Override
    public List<Map<List<String>, List<String>>> getTabComplete() {
        return List.of(
                Map.of(List.of(), getName()),
                Map.of(
                        getName(), List.of("enable", "disable", "peaceful", "easy", "normal", "hard")
                ),
                Map.of(List.of("peaceful", "easy", "normal", "hard"), List.of("<damage per second>"))
        );
    }
}
