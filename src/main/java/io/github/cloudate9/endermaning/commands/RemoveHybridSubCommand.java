package io.github.cloudate9.endermaning.commands;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.MessagesConfig;
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
public class RemoveHybridSubCommand implements SubCommand {

    private final HybridConfig hybridConfig;
    private final JavaPlugin plugin;
    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final TeleporterObject teleporterObject;

    @Inject
    public RemoveHybridSubCommand(
            HybridConfig hybridConfig,
            JavaPlugin plugin,
            MessagesConfig messagesConfig,
            MiniMessage miniMessage,
            TeleporterObject teleporterObject
    ) {
        this.hybridConfig = hybridConfig;
        this.plugin = plugin;
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
        this.teleporterObject = teleporterObject;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {

        if (args.isEmpty()) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.removeHybridHelp)
            );
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args.get(0));
        if (player == null) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.playerNotFound)
                            .append(Component.text("\n"))
                            .append(miniMessage.deserialize(messagesConfig.removeHybridHelp))
            );
            return;
        }

        //Command sender tried to dupe remove.
        if (!hybridConfig.hybridList.contains(player.getUniqueId().toString())) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.success)
            );
            return;
        }

        hybridConfig.hybridList.remove(player.getUniqueId().toString());
        hybridConfig.legacyHybridList.remove(player.getUniqueId().toString());
        hybridConfig.uninformedHybridList.remove(player.getUniqueId().toString());

        if (sender instanceof Player) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.informAmNotHybrid)
            );
        }

        if (player instanceof Player) {

            teleporterObject.removeIfPresent((Player) player);

            HideNormals.showAllNormals((Player) player, plugin);

        } else hybridConfig.uninformedRevertedList.add(player.getUniqueId().toString());

        Config.saveConfig(hybridConfig);
        sender.sendMessage(
                miniMessage.deserialize(messagesConfig.success)
        );

    }

    @Override
    public List<String> getName() {
        return List.of("remove", "r");
    }

    @Override
    public List<Map<List<String>, List<String>>> getTabComplete() {
        //Use online players instead of offline players to prevent tanking server performance.
        List<String> onlineHybridNames = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hybridConfig.hybridList.contains(player.getUniqueId().toString()))
                onlineHybridNames.add(player.getName());
        }

        return List.of(
                Map.of(List.of(), getName()),
                Map.of(getName(), onlineHybridNames)
        );
    }
}
