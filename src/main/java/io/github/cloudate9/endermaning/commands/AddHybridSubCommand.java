package io.github.cloudate9.endermaning.commands;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import io.github.cloudate9.endermaning.hider.HideNormals;
import io.github.cloudate9.endermaning.teleporter.TeleporterObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class AddHybridSubCommand implements SubCommand {

    private final HybridConfig hybridConfig;
    private final JavaPlugin plugin;
    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final OptionsConfig optionsConfig;
    private final TeleporterObject teleporterObject;

    @Inject
    public AddHybridSubCommand(
            HybridConfig hybridConfig,
            JavaPlugin plugin,
            MessagesConfig messagesConfig,
            MiniMessage miniMessage,
            OptionsConfig optionsConfig,
            TeleporterObject teleporterObject
    ) {
        this.hybridConfig = hybridConfig;
        this.plugin = plugin;
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
        this.optionsConfig = optionsConfig;
        this.teleporterObject = teleporterObject;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {

        if (args.isEmpty()) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.addHybridHelp)
            );
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args.get(0));
        if (player == null) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.playerNotFound)
                            .append(Component.text("\n"))
                            .append(miniMessage.deserialize(messagesConfig.addHybridHelp))
            );
            return;
        }

        hybridConfig.uninformedRevertedList.remove(player.getUniqueId().toString());

        if (hybridConfig.hybridList.contains(player.getUniqueId().toString())) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.success)
            );
            return; //Command sender tried to dupe add.
        }

        hybridConfig.hybridList.add(player.getUniqueId().toString());


        if (sender instanceof Player) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.informAmHybrid)
            );
        }

        if (player instanceof Player) {

            teleporterObject.giveIfAbsent((Player) player);

            if (optionsConfig.hidePumpkinPlayersFromHybrids) HideNormals.hideAllNormals((Player) player, plugin);

        } else hybridConfig.uninformedHybridList.add(player.getUniqueId().toString());

        Config.saveConfig(hybridConfig);
        sender.sendMessage(
                miniMessage.deserialize(messagesConfig.success)
        );
    }

    @Override
    public List<String> getName() {
        return List.of("add", "a");
    }

    @Override
    public List<Map<List<String>, List<String>>> getTabComplete() {
        //Use online players instead of offline players to prevent tanking server performance.
        List<String> onlineNonHybridNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!hybridConfig.hybridList.contains(player.getUniqueId().toString()))
                onlineNonHybridNames.add(player.getName());
        }

        return List.of(
                Map.of(List.of(), getName()),
                Map.of(getName(), onlineNonHybridNames)
        );
    }
}
