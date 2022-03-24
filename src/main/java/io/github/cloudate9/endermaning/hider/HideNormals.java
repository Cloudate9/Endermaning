package io.github.cloudate9.endermaning.hider;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HideNormals {

    /**
     * Hide a player from a list of hybrids.
     * WARNING: This WILL break other plugins that hide players! (e.g. staff plugins)
     *
     * @param hybridUUIDList The list of hybrids to manage.
     * @param playerToHide   The player to hide.
     * @param plugin         The plugin instance calling this method.
     */
    public static void hideANormal(List<String> hybridUUIDList, Player playerToHide, JavaPlugin plugin) {
        for (String stringUUID : hybridUUIDList) {
            Player hybrid = Bukkit.getPlayer(UUID.fromString(stringUUID));
            hideANormal(hybrid, playerToHide, plugin);
        }
    }

    /**
     * Hide a player from a hybrid.
     * WARNING: This WILL break other plugins that hide players! (e.g. staff plugins)
     *
     * @param hybrid       The list of hybrids to manage.
     * @param playerToHide The player to hide.
     * @param plugin       The plugin instance calling this method.
     */
    public static void hideANormal(Player hybrid, Player playerToHide, JavaPlugin plugin) {
        if (hybrid == null || playerToHide == null) return;
        if (playerToHide.getInventory().getHelmet() == null ||
                playerToHide.getInventory().getHelmet().getType() != Material.CARVED_PUMPKIN) return;
        hybrid.hidePlayer(plugin, playerToHide);
    }

    /**
     * Hide all players from the given list of hybrids.
     * WARNING: This WILL break other plugins that hide players! (e.g. staff plugins)
     *
     * @param hybridUUIDList The list of hybrids to manage.
     * @param plugin         The plugin instance calling this method.
     */
    public static void hideAllNormals(List<String> hybridUUIDList, JavaPlugin plugin) {
        for (String stringUUID : hybridUUIDList) {
            Player hybrid = Bukkit.getPlayer(UUID.fromString(stringUUID));
            hideAllNormals(hybrid, plugin);
        }
    }

    /**
     * Hide all players from a singular hybrid.
     * WARNING: This WILL break other plugins that hide players! (e.g. staff plugins)
     *
     * @param hybrid The hybrid to manage.
     * @param plugin The plugin instance calling this method.
     */
    public static void hideAllNormals(Player hybrid, JavaPlugin plugin) {
        if (hybrid == null) return;

        for (Player bystander : Bukkit.getOnlinePlayers()) {
            if (bystander.getInventory().getHelmet() == null ||
                    bystander.getInventory().getHelmet().getType() != Material.CARVED_PUMPKIN) return;
            hybrid.hidePlayer(plugin, bystander);
        }
    }

    /**
     * Show a player to a list of hybrids.
     * WARNING: This WILL break other plugins that hide players! (e.g. staff plugins)
     *
     * @param hybridUUIDList The list of hybrids to manage.
     * @param playerToShow   The player to show.
     * @param plugin         The plugin instance calling this method.
     */
    public static void showANormal(List<String> hybridUUIDList, Player playerToShow, JavaPlugin plugin) {
        for (String stringUUID : hybridUUIDList) {
            Player hybrid = Bukkit.getPlayer(UUID.fromString(stringUUID));
            showANormal(hybrid, playerToShow, plugin);
        }
    }

    /**
     * Show a player to a hybrid.
     * WARNING: This WILL break other plugins that hide players! (e.g. staff plugins)
     *
     * @param hybrid       The list of hybrids to manage.
     * @param playerToShow The player to show.
     * @param plugin       The plugin instance calling this method.
     */
    public static void showANormal(Player hybrid, Player playerToShow, JavaPlugin plugin) {
        if (hybrid == null || playerToShow == null) return;
        if (playerToShow.getInventory().getHelmet() == null ||
                playerToShow.getInventory().getHelmet().getType() != Material.CARVED_PUMPKIN) return;
        hybrid.hidePlayer(plugin, playerToShow);
    }

    /**
     * Show the given list of hybrids all players.
     * WARNING: This WILL break other plugins that hide players! (e.g. staff plugins)
     *
     * @param hybridUUIDList The list of hybrids to manage.
     * @param plugin         The plugin instance calling this method.
     */
    public static void showAllNormals(List<String> hybridUUIDList, JavaPlugin plugin) {
        for (String stringUUID : hybridUUIDList) {
            Player hybrid = Bukkit.getPlayer(UUID.fromString(stringUUID));
            showAllNormals(hybrid, plugin);
        }
    }

    /**
     * A hybrids all players.
     * WARNING: This WILL break other plugins that hide players! (e.g. staff plugins)
     *
     * @param hybrid The hybrid to manage.
     * @param plugin The plugin instance calling this method.
     */
    public static void showAllNormals(Player hybrid, JavaPlugin plugin) {
        if (hybrid == null) return;

        for (Player bystander : Bukkit.getOnlinePlayers()) {
            if (bystander.getInventory().getHelmet() == null ||
                    bystander.getInventory().getHelmet().getType() != Material.CARVED_PUMPKIN) return;
            hybrid.showPlayer(plugin, bystander);
        }
    }


}
