package io.github.cloudate9.endermaning.listeners;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import io.github.cloudate9.endermaning.hider.HideNormals;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

@SuppressWarnings("ClassCanBeRecord")
public class HidePumpkinPlayers implements Listener {

    private final HybridConfig hybridConfig;
    private final JavaPlugin plugin;
    private final OptionsConfig optionsConfig;

    @Inject
    public HidePumpkinPlayers(HybridConfig hybridConfig, JavaPlugin plugin, OptionsConfig optionsConfig) {
        this.hybridConfig = hybridConfig;
        this.plugin = plugin;
        this.optionsConfig = optionsConfig;
    }

    @EventHandler
    public void eligiblePlayerJoin(PlayerJoinEvent e) {
        if (!optionsConfig.hidePumpkinPlayersFromHybrids) return;
        if (hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) return;
        if (e.getPlayer().getInventory().getHelmet() == null ||
                e.getPlayer().getInventory().getHelmet().getType() != Material.CARVED_PUMPKIN) return;
        HideNormals.hideANormal(hybridConfig.hybridList, e.getPlayer(), plugin);
    }

    @EventHandler
    public void equipPumpkin(PlayerArmorChangeEvent e) {
        if (!optionsConfig.hidePumpkinPlayersFromHybrids) return;
        if (hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) return;
        if (e.getSlotType() != PlayerArmorChangeEvent.SlotType.HEAD ||
                e.getNewItem() == null ||
                e.getNewItem().getType() != Material.CARVED_PUMPKIN) return;
        HideNormals.hideANormal(hybridConfig.hybridList, e.getPlayer(), plugin);
    }
}
