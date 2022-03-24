package io.github.cloudate9.endermaning.config;

import com.github.secretx33.sccfg.api.annotation.Comment;
import com.github.secretx33.sccfg.api.annotation.Configuration;
import com.github.secretx33.sccfg.api.annotation.Path;

import java.util.List;

@Configuration(
        value = "messages.yml",
        header = "Endermaning uses MiniMessage (https://docs.adventure.kyori.net/minimessage) for messages."
)
public class MessagesConfig {

    @Path("help")
    public String addHybridHelp = "<gold>/edm a <player name>: Makes a player a hybrid</gold>";

    @Path("help")
    public String damageKinHelp = "<gold>/edm dmgk [true/false]: Change if Players with Hybrid status can attack Endermen.";

    @Path("help")
    public String guiHelp = "<gold>/edm gui: Launches Endermaning's gui</gold>";

    @Path("help")
    public String hidePumpkinPlayersHelp = "<gold>/edm hpp [enable/disable]: " +
            "Change if players with Enderman hybrid status can see players with pumpkin heads on.</gold>";

    @Path("help")
    public String hostileEndermenHelp = "<gold>/edm hostile [enable/disable]: " +
            "Change if Endermen will attack players with Enderman hybrid status.</gold>";

    @Path("help")
    public String listHelp = "<gold>/edm ls: Lists all Enderman hybrids.</gold>";

    @Path("help")
    public String pearlDamageHelp = "<gold>/edm pd <damage per teleport>: " +
            "Set the amount of damage a hybrid should receive when using an Ender pearl.</gold>";

    @Path("help")
    public String reloadHelp = "<gold>/edm rl: " +
            "This doesn't actually do anything, " +
            "and is just here for people who like to reload after manual changes to a config.</gold>";

    @Path("help")
    public String removeHybridHelp = "<gold>/edm r <player name>: Remove a player from being a hybrid</gold>";

    @Path("help")
    public String silkHandsHelp = "<gold>/edm silk [enable/disable]: " +
            "Change if Players with Enderman hybrid status " +
            "will get a silk touched version of the block if block is broken with hand.</gold>";

    @Path("help")
    public String teleportCooldownHelp = "<gold>/edm tpc <cooldown in seconds>: " +
            "Change the cooldown time for teleportation! At least 1 second of cooldown is needed.</gold>";

    @Path("help")
    public String teleporterHelp = "<gold>/edm tp [enable/disable/random/infinite]: " +
            "Change the Enderman hybrid teleporting mechanism.</gold>";

    @Path("help")
    public String waterDamageEnabledHelp = "<gold>/edm water [enable/disable]: " +
            "Change if players with Enderman hybrid status get hurt in rain or water.</gold>";

    @Path("help")
    public String waterDamageValueHelp = "<gold>/edm water [peaceful/easy/normal/hard] <damage per tick>: " +
            "Change the amount of damage players with Enderman hybrid status get hurt by in rain or water.</gold>";


    @Comment("The message sent to notify a player that they have become a hybrid")
    public String informAmHybrid = "<gold>You have become an Enderman hybrid!</gold>";

    @Comment("The message sent to notify a player that they are no longer a hybrid")
    public String informAmNotHybrid = "<gold>You are no longer an Enderman hybrid!</gold>";

    @Comment("Message sent when a negative value is given in a command")
    public String negativeNumberWarning = "<yellow>A value you entered is negative! " +
            "The command will still process, but unexpected behaviour might occur.<yellow>";

    @Comment("Message sent when console use is disallowed")
    public String noConsoleUse = "<red>You can't use this command from the console!<red>";

    @Comment("Message sent when hybrids are requested, but none are found")
    public String noHybridsFound = "<green>No players are hybrids!</green>";

    @Comment("Message sent when a player does not have permission")
    public String noPerms = "<red>You do not have permission to configure Endermaning!</red>";

    @Comment("Message sent when players whom have not joined the server before are used for operations")
    public String playerNotFound = "<red>Player is not found! Get them to join the server before rerunning this command</red>";

    @Comment("Message sent when a plugin check fail")
    public String pluginUpdateCheckFail = "<red>Plugin check failed!</red>";

    @Comment("Message sent to eligible players and the console when an update is available.")
    public String pluginUpdateAvailable = "<gold>A new version of Endermaning is available at " +
            "https://www.curseforge.com/minecraft/bukkit-plugins/Endermaning/\n" +
            "If you don't see the update yet, come back in 24-48 hours. It should be ready by then.</gold>";

    @Comment("Message sent to the console when the plugin is up to date.")
    public String pluginUpToDate = "<green>Endermaning is up to date!</green>";

    @Comment("Generic command success message")
    public String success = "<green>Operation success!</green>";

    @Comment("Message sent when teleporter is on cooldown")
    public String teleporterOnCooldown = "<red>Teleportation is on cooldown!</red>";

    public List<String> getAllHelpMessages() {
        return List.of(
                addHybridHelp,
                damageKinHelp,
                guiHelp,
                hidePumpkinPlayersHelp,
                hostileEndermenHelp,
                listHelp,
                pearlDamageHelp,
                reloadHelp,
                removeHybridHelp,
                silkHandsHelp,
                teleportCooldownHelp,
                teleporterHelp,
                waterDamageEnabledHelp,
                waterDamageValueHelp
        );
    }

}
