package io.github.cloudate9.endermaning.updater;

import io.github.cloudate9.endermaning.config.MessagesConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

@Singleton
public class EndermaningUpdateChecker implements UpdateChecker {

    private boolean firstCheck;
    private boolean updateFound;
    private BukkitTask checker = null;

    private final JavaPlugin plugin;
    private final MiniMessage miniMessage;
    private final MessagesConfig messagesConfig;

    @Inject
    public EndermaningUpdateChecker(JavaPlugin plugin, MiniMessage miniMessage, MessagesConfig messagesConfig) {
        this.plugin = plugin;
        this.miniMessage = miniMessage;
        this.messagesConfig = messagesConfig;
    }

    @Override
    public void checkForUpdate() {
        //Reset the countdown, if applicable.
        if (checker != null) checker.cancel();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Scanner readGit = new Scanner(
                            new InputStreamReader(
                                    new URL(
                                            "https://raw.githubusercontent.com/Cloudate9/InstaMineDeepslate/master/build.gradle.kts"
                                    ).openStream()
                            )
                    );

                    var version = "";
                    while (readGit.hasNext()) {
                        String line = readGit.nextLine();

                        if (line.startsWith("version = ")) {
                            //Targeted line example: version = "2.0.0"
                            //Is removal of suffix '"' required?
                            version = line.split("\"")[1];
                            break;
                        }
                    }

                    if (Objects.equals(version, plugin.getDescription().getVersion())) {
                        if (firstCheck) {
                            //No parsing, cause a String is required.
                            plugin.getLogger().info(messagesConfig.pluginUpToDate);
                            firstCheck = false;
                        }

                        checker = new BukkitRunnable() {
                            @Override
                            public void run() {
                                checkForUpdate();
                            }
                            //Arbitrary delay, to reduce unnecessary checks.
                        }.runTaskLaterAsynchronously(plugin, 576000);

                        return;
                    }

                    updateFound = true;

                    plugin.getLogger().info(messagesConfig.pluginUpdateAvailable);

                } catch (IOException ex) {
                    plugin.getLogger().info(messagesConfig.pluginUpdateCheckFail);

                    checker = new BukkitRunnable() {
                        @Override
                        public void run() {
                            checkForUpdate();
                        }
                        //Arbitrary delay, to reduce unnecessary checks.
                    }.runTaskLaterAsynchronously(plugin, 576000);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    @Override
    public boolean isUpdateAvailable() {
        return updateFound;
    }
}
