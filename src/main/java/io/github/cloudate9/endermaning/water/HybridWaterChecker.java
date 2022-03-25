package io.github.cloudate9.endermaning.water;

import io.github.cloudate9.endermaning.config.OptionsConfig;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class HybridWaterChecker implements WaterChecker {

    private final JavaPlugin plugin;
    private final List<Player> playersToCheck = new ArrayList<>();
    private final OptionsConfig optionsConfig;
    private BukkitTask checker = null;

    @Inject
    public HybridWaterChecker(JavaPlugin plugin, OptionsConfig optionsConfig) {
        this.plugin = plugin;
        this.optionsConfig = optionsConfig;
    }

    @Override
    public void addToCheck(Player player) {
        playersToCheck.add(player);
        if (checker == null) startCheck();
    }

    @Override
    public void removeFromCheck(Player player) {
        playersToCheck.remove(player);
        if (playersToCheck.isEmpty()) stopCheck();
    }

    private void startCheck() {
        checker = new BukkitRunnable() {

            @Override
            public void run() {
                if (!optionsConfig.waterDamageEnabled) {
                    stopCheck();
                    return;
                }
                for (Player player : playersToCheck) {
                    if (player == null) {
                        removeFromCheck(null);
                        continue;
                    }
                    if (player.isDead() || player.isInvulnerable()) continue;

                    if (player.isInWaterOrRainOrBubbleColumn())
                        switch (player.getWorld().getDifficulty()) {
                            case PEACEFUL -> player.damage(optionsConfig.waterPeacefulDamage);
                            case EASY -> player.damage(optionsConfig.waterEasyDamage);
                            case HARD -> player.damage(optionsConfig.waterHardDamage);
                            //Make this default instead of normal jic custom difficulty.
                            default -> player.damage(optionsConfig.waterNormalDamage);
                        }
                }
                startCheck(); //Recursive
            }
        }.runTaskLater(plugin, 10L); //Run every 10 ticks as specified in the rest of the plugin.
    }

    private void stopCheck() {
        if (checker != null) {
            checker.cancel();
            checker = null;
        }
    }
}
