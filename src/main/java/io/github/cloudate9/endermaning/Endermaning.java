package io.github.cloudate9.endermaning;

import com.github.secretx33.sccfg.Config;
import io.github.cloudate9.endermaning.commands.DaggerCommandComponent;
import io.github.cloudate9.endermaning.config.*;
import io.github.cloudate9.endermaning.listeners.*;
import io.github.cloudate9.endermaning.teleporter.DaggerTeleporterComponent;
import io.github.cloudate9.endermaning.teleporter.TeleporterObject;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class Endermaning extends JavaPlugin {

    private HybridConfig hybridConfig;
    private MessagesConfig messagesConfig;
    private OptionsConfig optionsConfig;
    private TeleporterObject teleporterObject;


    public void onEnable() {
        // Constructors are not allowed for plugins. Do manually DI.
        ConfigComponent configComponent = DaggerConfigComponent.create();
        this.hybridConfig = configComponent.getHybridConfig();
        this.messagesConfig = configComponent.getMessagesConfig();
        this.optionsConfig = configComponent.getOptionsConfig();
        this.teleporterObject = DaggerTeleporterComponent.create().getTeleporterManager();

        this.legacyConverter();
        new Metrics(this, 11128);

        //Currently, update checker is called in the UpdateInformer class, in the Checker's init method.
        //This is due to me not being able to properly implement a singleton in Dagger 2 for the UpdateChecker (impl) class.

        ListenerComponent listenerComponent = DaggerListenerComponent.create();
        Bukkit.getPluginManager().registerEvents(listenerComponent.getOfflineHybridStatusInformer(), this);
        Bukkit.getPluginManager().registerEvents(listenerComponent.getHidePumpkinPlayers(), this);
        Bukkit.getPluginManager().registerEvents(listenerComponent.getHybridTeleportation(), this);
        Bukkit.getPluginManager().registerEvents(listenerComponent.getSilkHands(), this);
        Bukkit.getPluginManager().registerEvents(listenerComponent.getStopEndermenHostility(), this);
        Bukkit.getPluginManager().registerEvents(listenerComponent.getTeleporterAllocator(), this);
        Bukkit.getPluginManager().registerEvents(listenerComponent.getUpdateInformer(), this);
        Bukkit.getPluginManager().registerEvents(listenerComponent.getWaterRegisterer(), this);

        Objects.requireNonNull(getCommand("endermaning"))
                .setExecutor(DaggerCommandComponent.create().getSubCommandManager());

        //WARNING: Pumpkin checker and water checker is not checked on reload!

    }

    @Override
    public void onDisable() {
        Config.saveConfigs(hybridConfig, messagesConfig, optionsConfig);
    }

    private void legacyConverter() {
        FileConfiguration legacyConfig = this.getConfig();

        for (String key : legacyConfig.getKeys(false)) {
            if (key.equals("enderman")) continue; //Handled later below

            //Most option names changed in v2.0.0
            switch (key) {
                case "endermenCooldown" -> optionsConfig.hybridTeleportCooldown = legacyConfig.getInt(key);
                case "hidePumpkinPlayers" -> optionsConfig.hidePumpkinPlayersFromHybrids = legacyConfig.getBoolean(key);
                case "hostileEndermen" -> optionsConfig.endermenHostileToHybrids = legacyConfig.getBoolean(key);
                case "pearlDamage" -> optionsConfig.hybridPearlDamage = legacyConfig.getBoolean(key) ? 4 : 0;

                //No need to mess with teleporter enabled.
                case "randomTeleport" -> optionsConfig.randomInsteadOfInfiniteTeleporter = legacyConfig.getBoolean(key);
                case "silkHands" -> optionsConfig.hybridHasSilkTouchHands = legacyConfig.getBoolean(key);

                //No need to mess with default values for water damage.
                case "waterRainHurt" -> optionsConfig.waterDamageEnabled = legacyConfig.getBoolean(key);
                //Ignore all other values, as those are custom user added values.
            }
        }

        ConfigurationSection configurationSection = legacyConfig.getConfigurationSection("enderman");

        if (configurationSection == null || configurationSection.getKeys(false).size() == 0) return;

        //Legacy names will not be deeper.
        List<String> legacyHybridList = new ArrayList<>(configurationSection.getKeys(false));
        if (legacyHybridList.isEmpty()) return;

        List<String> unUpdatedLegacyPlayers = new ArrayList<>();

        //If they are not online, store in uninformed enderman. We want ot give them the new teleporter.
        for (String stringUUID : legacyHybridList) {

            if (!hybridConfig.hybridList.contains(stringUUID)) hybridConfig.hybridList.add(stringUUID);

            Player potentialPlayer = Bukkit.getPlayer(UUID.fromString(stringUUID));
            if (potentialPlayer == null) unUpdatedLegacyPlayers.add(stringUUID);
            else teleporterObject.legacyReplacer(potentialPlayer);
        }

        hybridConfig.legacyHybridList = unUpdatedLegacyPlayers;
        Config.saveConfig(hybridConfig);
        //Now get rid of the config.yml

        //noinspection ResultOfMethodCallIgnored
        new File(getDataFolder().getAbsolutePath() + File.separator + "config.yml").delete();
    }


}
