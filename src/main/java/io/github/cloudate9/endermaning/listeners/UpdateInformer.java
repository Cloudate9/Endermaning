package io.github.cloudate9.endermaning.listeners;

import io.github.cloudate9.endermaning.config.MessagesConfig;
import io.github.cloudate9.endermaning.updater.UpdateChecker;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

@SuppressWarnings("ClassCanBeRecord")
public class UpdateInformer implements Listener {

    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final UpdateChecker updateChecker;

    @Inject
    public UpdateInformer(MessagesConfig messagesConfig, MiniMessage miniMessage, UpdateChecker updateChecker) {
        this.messagesConfig = messagesConfig;
        this.miniMessage = miniMessage;
        this.updateChecker = updateChecker;
    }


    @EventHandler
    public void eligiblePlayerJoin(PlayerJoinEvent e) {
        if (!updateChecker.isUpdateAvailable()) return;
        if (!e.getPlayer().hasPermission("endermaning.update")) return;
        e.getPlayer().sendMessage(
                miniMessage.deserialize(messagesConfig.pluginUpdateAvailable)
                        //Preferably move ClickEvent to message config with mini message, once it works.
                        .clickEvent(
                                ClickEvent.clickEvent(
                                        ClickEvent.Action.OPEN_URL,
                                        "https://www.curseforge.com/minecraft/bukkit-plugins/endermaning"
                                )
                        )
        );
    }

}
