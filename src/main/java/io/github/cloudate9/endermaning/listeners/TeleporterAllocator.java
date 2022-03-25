package io.github.cloudate9.endermaning.listeners;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import io.github.cloudate9.endermaning.teleporter.TeleporterObject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

@SuppressWarnings("ClassCanBeRecord")
public class TeleporterAllocator implements Listener {

    private final HybridConfig hybridConfig;
    private final OptionsConfig optionsConfig;
    private final TeleporterObject teleporterObject;


    @Inject
    public TeleporterAllocator(
            HybridConfig hybridConfig,
            OptionsConfig optionsConfig,
            TeleporterObject teleporterObject
    ) {
        this.hybridConfig = hybridConfig;
        this.optionsConfig = optionsConfig;
        this.teleporterObject = teleporterObject;
    }

    @EventHandler
    public void ensureHasTeleporterJoin(PlayerJoinEvent e) {
        if (hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) {
            if (hybridConfig.legacyHybridList.contains(e.getPlayer().getUniqueId().toString())) {

                teleporterObject.legacyReplacer(e.getPlayer());
                hybridConfig.legacyHybridList.remove(e.getPlayer().getUniqueId().toString());
                Config.saveConfig(hybridConfig);

            } else teleporterObject.giveIfAbsent(e.getPlayer());
        }
    }

    @EventHandler
    public void ensureNoHaveTeleporterJoin(PlayerJoinEvent e) {
        if (!optionsConfig.teleporterEnabled || !hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString()))
            teleporterObject.removeIfPresent(e.getPlayer());
    }

    //NOTE: There is absolutely nothing stopping a player from moving a teleporter to another inventory (e.g. a chest).
    //It is hard to implement, and in my testing, easy to bypass by just spamming the teleporter.

    @EventHandler
    public void giveTeleporterOnDiscard(PlayerDropItemEvent e) {
        ItemStack potentialTeleporter = e.getItemDrop().getItemStack();
        if (teleporterObject.getInfiniteTeleporter().equals(potentialTeleporter)
                || teleporterObject.getRandomTeleporter().equals(potentialTeleporter)) {

            e.getItemDrop().remove(); //Don't cancel. Maybe player wants to rearrange inventory.
            teleporterObject.giveIfAbsent(e.getPlayer());
        }
    }

    @EventHandler
    public void nonPlayerTeleporterMove(InventoryMoveItemEvent e) {
        ItemStack potentialTeleporter = e.getItem();
        if (teleporterObject.getInfiniteTeleporter().equals(potentialTeleporter)
                || teleporterObject.getRandomTeleporter().equals(potentialTeleporter)) {

            e.setCancelled(true); //Teleporter should not exist outside of inventory.
        }
    }

    @EventHandler
    public void removeStrayItem(EntityPickupItemEvent e) {
        ItemStack potentialTeleporter = e.getItem().getItemStack();
        if (teleporterObject.getInfiniteTeleporter().equals(potentialTeleporter)
                || teleporterObject.getRandomTeleporter().equals(potentialTeleporter)) {
            e.setCancelled(true);
            e.getItem().remove(); //Teleporter should not exist outside of inventory.
        }
    }

    @EventHandler
    public void removeTeleporterOnDeath(PlayerDeathEvent e) {
        e.getDrops().remove(teleporterObject.getRandomTeleporter());
        e.getDrops().remove(teleporterObject.getInfiniteTeleporter());
    }

    @EventHandler
    public void playerEndermenRespawn(PlayerRespawnEvent e) {
        if (optionsConfig.teleporterEnabled &&
                hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) {
            teleporterObject.giveIfAbsent(e.getPlayer());
        }
    }


}
