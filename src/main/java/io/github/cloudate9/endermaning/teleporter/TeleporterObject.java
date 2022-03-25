package io.github.cloudate9.endermaning.teleporter;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface TeleporterObject {

    /**
     * Gives a player the required teleporter, if they don't already have one, and if they are a hybrid.
     *
     * @param player The player to check.
     */
    void giveIfAbsent(Player player);

    /**
     * Removes legacy teleporters, if found.
     * Automatically calls {@link TeleporterObject#giveIfAbsent(Player)}.
     *
     * @param player The player to check
     */
    void legacyReplacer(Player player);

    /**
     * Removes a player's teleporter, if they have one, regardless if they are a hybrid.
     *
     * @param player The player to check.
     */
    void removeIfPresent(Player player);

    /**
     * @return A copy of the infinite teleporter.
     */
    ItemStack getInfiniteTeleporter();

    /**
     *
     * @return A copy of the random teleporter.
     */
    ItemStack getRandomTeleporter();

}
