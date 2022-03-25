package io.github.cloudate9.endermaning.listeners;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import javax.inject.Inject;

@SuppressWarnings("ClassCanBeRecord")
public class OfflineHybridStatusInformer implements Listener {

    private final HybridConfig hybridConfig;
    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;

    @Inject
    public OfflineHybridStatusInformer(
            HybridConfig hybridConfig,
            MessagesConfig messagesConfig,
            MiniMessage miniMessage
    ) {
        this.hybridConfig = hybridConfig;
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
    }

    @EventHandler
    public void informPreviouslyOfflineHybrid(PlayerJoinEvent e) {
        if (hybridConfig.uninformedHybridList.contains(e.getPlayer().getUniqueId().toString())) {
            e.getPlayer().sendMessage(
                    miniMessage.deserialize(messagesConfig.informAmHybrid)
            );

            hybridConfig.uninformedHybridList.remove(e.getPlayer().getUniqueId().toString());
            Config.saveConfig(hybridConfig);
        }
    }

    @EventHandler
    public void informPreviouslyOfflineReverted(PlayerJoinEvent e) {
        if (hybridConfig.uninformedRevertedList.contains(e.getPlayer().getUniqueId().toString())) {
            e.getPlayer().sendMessage(
                    miniMessage.deserialize(messagesConfig.informAmNotHybrid)
            );

            hybridConfig.uninformedRevertedList.remove(e.getPlayer().getUniqueId().toString());
            Config.saveConfig(hybridConfig);
        }
    }

}