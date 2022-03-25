package io.github.cloudate9.endermaning.listeners;

import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import io.github.cloudate9.endermaning.water.WaterChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

@SuppressWarnings("ClassCanBeRecord")
public class WaterRegisterer implements Listener {

    private final HybridConfig hybridConfig;
    private final OptionsConfig optionsConfig;
    private final WaterChecker waterChecker;

    @Inject
    public WaterRegisterer(HybridConfig hybridConfig, OptionsConfig optionsConfig, WaterChecker waterChecker) {
        this.hybridConfig = hybridConfig;
        this.optionsConfig = optionsConfig;
        this.waterChecker = waterChecker;
    }


    @EventHandler
    public void hybridJoin(PlayerJoinEvent e) {
        if (!optionsConfig.waterDamageEnabled) return;
        if (hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) {
            waterChecker.addToCheck(e.getPlayer());
        }
    }

    @EventHandler
    public void hybridLeave(PlayerQuitEvent e) {
        if (!optionsConfig.waterDamageEnabled) return;
        if (hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) {
            waterChecker.removeFromCheck(e.getPlayer());
        }
    }


}
