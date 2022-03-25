package io.github.cloudate9.endermaning.water;

import org.bukkit.entity.Player;

public interface WaterChecker {

    /**
     * Add a player to be checked if they are in water.
     * @param player The player to check.
     */
    void addToCheck(Player player);

    /**
     * Remove a player from being checked if they are in water.
     * @param player The player to check.
     */
    void removeFromCheck(Player player);

}
