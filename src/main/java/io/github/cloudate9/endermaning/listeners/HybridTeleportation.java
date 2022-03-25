package io.github.cloudate9.endermaning.listeners;

import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import io.github.cloudate9.endermaning.teleporter.TeleporterObject;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HybridTeleportation implements Listener {

    private final HybridConfig hybridConfig;
    private final JavaPlugin plugin;
    private final MessagesConfig messagesConfig;
    private final MiniMessage miniMessage;
    private final OptionsConfig optionsConfig;
    private final TeleporterObject teleporterObject;

    private final Map<Player, Integer> cooldown = new HashMap<>();

    @Inject
    public HybridTeleportation(
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

    //Reminder: There is no sound for pearl teleportation in vanilla minecraft.

    @EventHandler
    public void alterTeleportationDamage(PlayerTeleportEvent e) {
        if (!hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) return;
        if (e.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        teleporterObject.giveIfAbsent(e.getPlayer());

        //Prevent damage by cancelling the pearl tp, then manually do so instead.
        e.setCancelled(true);
        e.getPlayer().teleport(e.getTo());
        e.getPlayer().damage(optionsConfig.hybridPearlDamage);
    }

    private void decreaseCooldown(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (cooldown.containsKey(player)) {
                    int cooldownInSeconds = Math.min(cooldown.get(player) - 1, optionsConfig.hybridTeleportCooldown);
                    if (cooldownInSeconds > 0) {
                        cooldown.replace(player, cooldownInSeconds);
                        decreaseCooldown(player);
                    } else cooldown.remove(player);
                }
            }
        }.runTaskLater(plugin, 20L);
    }

    @EventHandler
    public void useHybridTeleporter(PlayerInteractEvent e) {
        if (!optionsConfig.teleporterEnabled) return; //Should never happen, as player shouldn't even have teleporter.
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) return;
        if (e.getItem() == null) return;

        if (e.getItem().isSimilar(teleporterObject.getInfiniteTeleporter())) {

            //This shouldn't even happen
            if (!optionsConfig.teleporterEnabled || !hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) {
                e.setCancelled(true);
                e.getItem().setAmount(0);
                return;
            }

            if (cooldown.containsKey(e.getPlayer())) {
                e.setCancelled(true);
                e.getPlayer().setCooldown(Material.ENDER_PEARL, 0);
                e.getPlayer().sendMessage(
                        miniMessage.deserialize(messagesConfig.teleporterOnCooldown)
                );

            } else {
                //Known bug: If the player clicks another inventory with the pearl, the cooldown will be started.
                //We change the amount instead of using teleporterObject#giveIfAbsent, as we want to preserve the slot of the teleporter.
                e.getItem().setAmount(2);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        //Ensure that player only has one teleporter.
                        e.getItem().setAmount(1);
                    }
                }.runTaskLater(plugin, 1L);
                cooldown.put(e.getPlayer(), optionsConfig.hybridTeleportCooldown);
                decreaseCooldown(e.getPlayer());

            }

        } else if (e.getItem().isSimilar(teleporterObject.getRandomTeleporter())) {

            e.setCancelled(true); //Don't give player the illusion that they can control where they are going.

            //This shouldn't even happen
            if (!optionsConfig.teleporterEnabled || !hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) {
                e.getItem().setAmount(0);
                return;
            }

            if (cooldown.containsKey(e.getPlayer())) {
                e.getPlayer().sendMessage(
                        miniMessage.deserialize(messagesConfig.teleporterOnCooldown)
                );
                e.getPlayer().setCooldown(Material.ENDER_PEARL, 0);
                return;
            }

            //Only start cooldown on successful teleport, as there is a chance that teleport fails.

            //Arbitrary teleportation attempts of 50. Just fail if it's not possible.
            for (int i = 0; i < 50; i++) {
                //Move the player -200 to 200 blocks in the x and z axis.
                int changeX = (new Random().nextInt(2) == 0 ? 1 : -1) * new Random().nextInt(200);
                int changeZ = (new Random().nextInt(2) == 0 ? 1 : -1) * new Random().nextInt(200);
                Location playerLoc = e.getPlayer().getLocation();

                //The magic number here comes from the (y level of the lowest possible block + the y level for the highest possible block) / 2.
                for (int j = 0; j < 128; j++) {
                    if (playerLoc.getBlockX() + i <= 320) {

                        switch (playerLoc.getWorld().getBlockAt(
                                playerLoc.getBlockX() + changeX,
                                playerLoc.getBlockY() + j,
                                playerLoc.getBlockZ() + changeZ
                        ).getType()) {

                            //Air, water, lava, and blocks that indicate that a player is underwater
                            case AIR, CAVE_AIR, VOID_AIR, WATER, WATER_CAULDRON, LAVA, LAVA_CAULDRON,
                                    SEAGRASS, TALL_SEAGRASS, KELP, BRAIN_CORAL,
                                    BRAIN_CORAL_FAN, BUBBLE_CORAL, BUBBLE_CORAL_FAN,
                                    FIRE_CORAL, FIRE_CORAL_FAN, HORN_CORAL, HORN_CORAL_FAN,
                                    TUBE_CORAL, TUBE_CORAL_FAN -> {
                            }

                            default -> {
                                //Check for air so that player doesn't suffocate.
                                if (playerLoc.getWorld().getBlockAt(
                                        playerLoc.getBlockX() + changeX,
                                        playerLoc.getBlockY() + j,
                                        playerLoc.getBlockZ() + changeZ
                                ).getType() == Material.AIR) {

                                    e.getPlayer().teleport(
                                            new Location(
                                                    playerLoc.getWorld(),
                                                    playerLoc.getX() + changeX,
                                                    playerLoc.getY() + j,
                                                    playerLoc.getZ() + changeZ
                                            )
                                    );

                                    cooldown.put(e.getPlayer(), optionsConfig.hybridTeleportCooldown);
                                    decreaseCooldown(e.getPlayer());

                                    return;
                                }
                            }
                        }
                    }

                    if (playerLoc.getBlockX() - i <= -64) {

                        switch (playerLoc.getWorld().getBlockAt(
                                playerLoc.getBlockX() + changeX,
                                playerLoc.getBlockY() - j,
                                playerLoc.getBlockZ() + changeZ
                        ).getType()) {

                            //Air, water, lava, and blocks that indicate that a player is underwater
                            case AIR, CAVE_AIR, VOID_AIR, WATER, WATER_CAULDRON, LAVA, LAVA_CAULDRON,
                                    SEAGRASS, TALL_SEAGRASS, KELP, BRAIN_CORAL,
                                    BRAIN_CORAL_FAN, BUBBLE_CORAL, BUBBLE_CORAL_FAN,
                                    FIRE_CORAL, FIRE_CORAL_FAN, HORN_CORAL, HORN_CORAL_FAN,
                                    TUBE_CORAL, TUBE_CORAL_FAN -> {
                            }

                            default -> {
                                //Check for air so that player doesn't suffocate.
                                if (playerLoc.getWorld().getBlockAt(
                                        playerLoc.getBlockX() + changeX,
                                        playerLoc.getBlockY() + j,
                                        playerLoc.getBlockZ() + changeZ
                                ).getType() == Material.AIR) {

                                    e.getPlayer().teleport(
                                            new Location(
                                                    playerLoc.getWorld(),
                                                    playerLoc.getX() + changeX,
                                                    playerLoc.getY() - j,
                                                    playerLoc.getZ() + changeZ
                                            )
                                    );

                                    cooldown.put(e.getPlayer(), optionsConfig.hybridTeleportCooldown);
                                    decreaseCooldown(e.getPlayer());

                                    return;
                                } //This amount of nesting looks disgusting. Maybe fix in the future.
                            }
                        }
                    }
                }
            }
        }
    }

}
