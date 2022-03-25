package io.github.cloudate9.endermaning.listeners;

import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import javax.inject.Inject;

@SuppressWarnings("ClassCanBeRecord")
public class StopEndermenHostility implements Listener {

    private final HybridConfig hybridConfig;
    private final OptionsConfig optionsConfig;

    @Inject
    public StopEndermenHostility(HybridConfig hybridConfig, OptionsConfig optionsConfig) {
        this.hybridConfig = hybridConfig;
        this.optionsConfig = optionsConfig;
    }

    @EventHandler
    public void endermanTargetHybrid(EntityTargetLivingEntityEvent e) {
        if (optionsConfig.endermenHostileToHybrids) return;
        if (e.getEntityType() != EntityType.ENDERMAN || e.getTarget() == null ||
                e.getTarget().getType() != EntityType.PLAYER) return;

        if (hybridConfig.hybridList.contains(e.getTarget().getUniqueId().toString())) {
            e.setCancelled(true); //Don't make target null, as the enderman could've previously targeted a non-hybrid.
        }
    }

    @EventHandler
    public void hybridAttackEnderman(EntityDamageByEntityEvent e) {
        if (optionsConfig.hybridsCanAttackEndermen) return;
        if (e.getEntityType() != EntityType.ENDERMAN || e.getDamager().getType() != EntityType.PLAYER) return;

        if (hybridConfig.hybridList.contains(e.getDamager().getUniqueId().toString())) {
            e.setCancelled(true);
        }
    }

}
