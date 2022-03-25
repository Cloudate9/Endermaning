package io.github.cloudate9.endermaning.teleporter;

import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class HybridTeleporterObject implements TeleporterObject {

    private final JavaPlugin plugin;
    private final HybridConfig hybridConfig;
    private final OptionsConfig optionsConfig;

    private final ItemStack infiniteTeleporter = new ItemStack(Material.ENDER_PEARL);
    private final ItemStack randomTeleporter = new ItemStack(Material.ENDER_PEARL);

    @Inject
    public HybridTeleporterObject(JavaPlugin plugin, HybridConfig hybridConfig, OptionsConfig optionsConfig) {
        this.plugin = plugin;
        this.hybridConfig = hybridConfig;
        this.optionsConfig = optionsConfig;

        ItemMeta infiniteTeleporterMeta = infiniteTeleporter.getItemMeta();
        infiniteTeleporterMeta.lore(List.of(Component.text("Infinite Teleporter")));

        infiniteTeleporterMeta.displayName(
                Component.text("Teleporter", TextColor.color(255, 85, 255)) //Light purple
        );

        infiniteTeleporter.setItemMeta(infiniteTeleporterMeta);

        ItemMeta randomTeleporterMeta = randomTeleporter.getItemMeta();
        randomTeleporterMeta.lore(List.of(Component.text("Random Teleporter")));

        randomTeleporterMeta.displayName(
                Component.text("Teleporter", TextColor.color(255, 85, 255)) //Light purple
        );

        randomTeleporter.setItemMeta(randomTeleporterMeta);
    }

    @Override
    public ItemStack getInfiniteTeleporter() {
        return infiniteTeleporter;
    }

    @Override
    public ItemStack getRandomTeleporter() {
        return randomTeleporter;
    }

    @Override
    public void giveIfAbsent(Player player) {
        if (!hybridConfig.hybridList.contains(player.getUniqueId().toString())) return;
        if (!optionsConfig.teleporterEnabled) return;
        if (optionsConfig.randomInsteadOfInfiniteTeleporter) {

            player.getInventory().remove(infiniteTeleporter);
            if (!player.getInventory().contains(randomTeleporter)) player.getInventory().addItem(randomTeleporter);

        } else { //Infinite pearl

            player.getInventory().remove(randomTeleporter);
            if (!player.getInventory().contains(infiniteTeleporter)) player.getInventory().addItem(infiniteTeleporter);
        }

        if (player.getInventory().contains(randomTeleporter) || player.getInventory().contains(infiniteTeleporter))
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                giveIfAbsent(player);
            }
        }.runTaskLater(plugin, 40L); //Delay preventing lag
    }

    @Override
    public void legacyReplacer(Player player) {

        for (ItemStack playerItem : player.getInventory()) {
            if (playerItem == null || !playerItem.hasItemMeta() ||
                    !playerItem.getItemMeta().hasLore() ||
                    Objects.requireNonNull(playerItem.getItemMeta().lore()).size() != 2) continue;

            if (playerItem.getType() == Material.CHORUS_FRUIT) {

                //For some reason, the actual lore bit for each component is stored in children.
                if (Objects.requireNonNull(Objects.requireNonNull(playerItem.lore()).get(0).children().get(0))
                        .equals(Component.text("This is your teleportation device!")) &&

                        Objects.requireNonNull(Objects.requireNonNull(playerItem.lore()).get(1).children().get(0))
                                .equals(Component.text("Right click to randomly teleport!"))) {

                    player.getInventory().remove(playerItem);
                }

            } else if (playerItem.getType() == Material.ENDER_PEARL) {

                if (Objects.requireNonNull(Objects.requireNonNull(playerItem.lore()).get(0).children().get(0))
                        .equals(Component.text("This is your teleportation device!")) &&

                        Objects.requireNonNull(Objects.requireNonNull(playerItem.lore()).get(0).children().get(0))
                                .equals(Component.text("Use like a normal Ender Pearl!"))) {

                    player.getInventory().remove(playerItem);
                }
            }
        }

        giveIfAbsent(player);
    }

    @Override
    public void removeIfPresent(Player player) {
        player.getInventory().remove(infiniteTeleporter);
        player.getInventory().remove(randomTeleporter);
    }
}
