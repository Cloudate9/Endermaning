package io.github.cloudate9.endermaning.commands;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import io.github.cloudate9.endermaning.config.MessagesConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.List;

public class GuiSubCommand implements SubCommand {

    private String storedCommand = "";

    private final JavaPlugin plugin;
    private final MiniMessage miniMessage;
    private final MessagesConfig messagesConfig;

    @Inject
    public GuiSubCommand(JavaPlugin plugin, MiniMessage miniMessage, MessagesConfig messagesConfig) {
        this.plugin = plugin;
        this.miniMessage = miniMessage;
        this.messagesConfig = messagesConfig;
    }

    @Override
    public void call(CommandSender sender, List<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.noConsoleUse)
            );
            return;
        }

        if (!sender.hasPermission("endermaning.configure")) {
            sender.sendMessage(
                    miniMessage.deserialize(messagesConfig.noPerms)
            );
            return;
        }

        ItemStack backArrowItemStack = ItemBuilder.from(Material.ARROW)
                .name(Component.text("Back"))
                .build();

        Gui mainGui = Gui.gui()
                .title(Component.text("Endermaning GUI"))
                .rows(6)
                .create();
        mainGui.setDefaultClickAction(e -> e.setCancelled(true));

        Gui trueOrFalseGui = Gui.gui()
                .title(Component.text("Status"))
                .type(GuiType.HOPPER)
                .create();

        trueOrFalseGui.setDefaultClickAction(e -> e.setCancelled(true));

        GuiItem trueItem = ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE)
                .name(Component.text("Enable"))
                .asGuiItem(e -> ((Player) sender).performCommand(storedCommand + " enable"));

        GuiItem falseItem = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE)
                .name(Component.text("Disable"))
                .asGuiItem(e -> ((Player) sender).performCommand(storedCommand + " disable"));

        trueOrFalseGui.setItem(0, trueItem);
        trueOrFalseGui.setItem(1, trueItem);
        trueOrFalseGui.setItem(2,
                ItemBuilder.from(backArrowItemStack).asGuiItem(e -> mainGui.open((Player) sender)));
        trueOrFalseGui.setItem(3, falseItem);
        trueOrFalseGui.setItem(4, falseItem);


        //Add hybrid item
        mainGui.setItem(2, 3, ItemBuilder.from(Material.NAME_TAG)
                .name(Component.text("Add Hybrid"))
                .asGuiItem(
                        e -> new AnvilGUI.Builder()
                                .onComplete(
                                        (player, text) -> {
                                            player.performCommand("edm a " + text);
                                            mainGui.open(player);
                                            return AnvilGUI.Response.close();
                                        }
                                )
                                .text("Player name")
                                .itemLeft(new ItemStack(Material.NAME_TAG))
                                .onRightInputClick(mainGui::open)
                                .title("Name of player to add")
                                .plugin(plugin)
                                .open((Player) sender)
                )
        );
        //Damage kin item
        mainGui.setItem(2, 5, ItemBuilder.from(Material.IRON_SWORD)
                .name(Component.text("Damage Kin"))
                .asGuiItem(
                        e -> {
                            storedCommand = "edm dmgk";
                            trueOrFalseGui.open((HumanEntity) sender);
                        }
                )
        );
        //Help item
        mainGui.setItem(3, 5, ItemBuilder.from(Material.WRITTEN_BOOK)
                .name(Component.text("Help"))
                .asGuiItem(e -> ((Player) sender).performCommand("edm help")
                )
        );
        //Hide pumpkin players item
        mainGui.setItem(4, 3, ItemBuilder.from(Material.CARVED_PUMPKIN)
                .name(Component.text("Hide Pumpkin Players"))
                .asGuiItem(e -> {
                            storedCommand = "edm hpp";
                            trueOrFalseGui.open((HumanEntity) sender);
                        }
                )
        );
        //Hostile Enderman item
        mainGui.setItem(4, 5, ItemBuilder.from(Material.ENDERMAN_SPAWN_EGG)
                .name(Component.text("Hostile Enderman"))
                .asGuiItem(e -> {
                            storedCommand = "edm hostile";
                            trueOrFalseGui.open((HumanEntity) sender);
                        }
                )
        );
        //List item
        mainGui.setItem(5, 2, ItemBuilder.from(Material.PAPER)
                .name(Component.text("List"))
                .asGuiItem(e -> ((Player) sender).performCommand("edm list"))
        );
        //Pearl damage item
        mainGui.setItem(5, 4, ItemBuilder.from(Material.ENDER_PEARL)
                .name(Component.text("Pearl Damage"))
                .asGuiItem(e -> new AnvilGUI.Builder()
                        .onComplete(
                                (player, text) -> {
                                    //Send the command regardless. It will be checked in the command itself.
                                    player.performCommand("edm pd " + text);
                                    try {
                                        Integer.parseInt(text);
                                        return AnvilGUI.Response.close();
                                    } catch (NumberFormatException ex) {
                                        return AnvilGUI.Response.text("Please enter a number");
                                    }
                                }
                        )
                        .text("Damage per teleport")
                        .itemLeft(new ItemStack(Material.ENDER_PEARL))
                        .onRightInputClick(mainGui::open)
                        .title("Damage per teleport")
                        .plugin(plugin)
                        .open((Player) sender)
                )
        );
        //Remove item
        mainGui.setItem(2, 7, ItemBuilder.from(Material.BARRIER)
                .name(Component.text("Remove"))
                .asGuiItem(e -> new AnvilGUI.Builder()
                        .onComplete(
                                (player, text) -> {
                                    player.performCommand("edm r " + text);
                                    return AnvilGUI.Response.close();
                                }
                        )
                        .text("Player name")
                        .itemLeft(new ItemStack(Material.NAME_TAG))
                        .onRightInputClick(mainGui::open)
                        .title("Name of player to remove")
                        .plugin(plugin)
                        .open((Player) sender)
                )
        );
        //Silk hands item
        mainGui.setItem(4, 7, ItemBuilder.from(Material.ENCHANTED_BOOK)
                .name(Component.text("Silk Hands"))
                .asGuiItem(e -> {
                            storedCommand = "edm silk";
                            trueOrFalseGui.open((HumanEntity) sender);
                        }
                )
        );
        //Teleport cooldown item
        mainGui.setItem(5, 6, ItemBuilder.from(Material.CLOCK)
                .name(Component.text("Teleport Cooldown"))
                .asGuiItem(e -> new AnvilGUI.Builder()
                        .onComplete(
                                (player, text) -> {
                                    player.performCommand("edm tpc " + text);
                                    try {
                                        Integer.parseInt(text);
                                        return AnvilGUI.Response.close();
                                    } catch (NumberFormatException ex) {
                                        return AnvilGUI.Response.text("Please enter a number");
                                    }
                                }
                        )
                        .text("Time in seconds")
                        .itemLeft(new ItemStack(Material.CLOCK))
                        .onRightInputClick(mainGui::open)
                        .title("Teleport cooldown in seconds")
                        .plugin(plugin)
                        .open((Player) sender)
                )
        );
        //Water damage item
        mainGui.setItem(5, 8, ItemBuilder.from(Material.WATER_BUCKET)
                .name(Component.text("Water Damage"))
                .asGuiItem(topLevel -> {

                            //GUI to select sub options
                            Gui selectionGui = Gui.gui()
                                    .title(Component.text("Configure water damage"))
                                    .type(GuiType.HOPPER)
                                    .create();
                            selectionGui.setDefaultClickAction(e -> e.setCancelled(true));

                            GuiItem statusItem = ItemBuilder.from(Material.BLUE_STAINED_GLASS_PANE)
                                    .name(Component.text("Enable/Disable"))
                                    .asGuiItem(mid -> {
                                                storedCommand = "edm wd";

                                                Gui customTrueOrFalseGui = Gui.gui()
                                                        .title(Component.text("Status"))
                                                        .type(GuiType.HOPPER)
                                                        .create();

                                                customTrueOrFalseGui.setDefaultClickAction(e -> e.setCancelled(true));

                                                //We can reuse the true and false gui items.
                                                customTrueOrFalseGui.setItem(0, trueItem);
                                                customTrueOrFalseGui.setItem(1, trueItem);
                                                customTrueOrFalseGui.setItem(2,
                                                        ItemBuilder.from(backArrowItemStack).asGuiItem(e -> selectionGui.open((Player) sender)));
                                                customTrueOrFalseGui.setItem(3, falseItem);
                                                customTrueOrFalseGui.setItem(4, falseItem);
                                                customTrueOrFalseGui.open((Player) sender);
                                            }
                                    );

                            GuiItem damageItem = ItemBuilder.from(Material.LIME_STAINED_GLASS_PANE)
                                    .name(Component.text("Tune damage amount"))
                                    .asGuiItem(mid -> {
                                                AnvilGUI.Builder builder = new AnvilGUI.Builder()
                                                        .onComplete(
                                                                (player, text) -> {
                                                                    player.performCommand(storedCommand + " " + text);
                                                                    try {
                                                                        Integer.parseInt(text);
                                                                        return AnvilGUI.Response.close();
                                                                    } catch (NumberFormatException ex) {
                                                                        return AnvilGUI.Response.text("Please enter a number");
                                                                    }
                                                                }
                                                        )
                                                        .text("Damage per 10 ticks")
                                                        .itemLeft(new ItemStack(Material.WATER_BUCKET))
                                                        .onRightInputClick(mainGui::open)
                                                        .title("Damage per 10 tick in water")
                                                        .plugin(plugin);

                                                Gui waterDamageGui = Gui.gui()
                                                        .title(Component.text("Water Damage"))
                                                        .type(GuiType.HOPPER)
                                                        .create();
                                                waterDamageGui.setDefaultClickAction(e -> e.setCancelled(true));
                                                waterDamageGui.setItem(0, ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE)
                                                        .name(Component.text("Peaceful"))
                                                        .asGuiItem(e -> {
                                                            storedCommand = "edm wd peaceful";
                                                            builder.open((Player) sender);
                                                        })
                                                );
                                                waterDamageGui.setItem(1, ItemBuilder.from(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
                                                        .name(Component.text("Easy"))
                                                        .asGuiItem(e -> {
                                                            storedCommand = "edm wd easy";
                                                            builder.open((Player) sender);
                                                        })
                                                );
                                                waterDamageGui.setItem(2, ItemBuilder.from(backArrowItemStack)
                                                        .asGuiItem(e -> selectionGui.open((Player) sender)
                                                        )
                                                );
                                                waterDamageGui.setItem(3, ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE)
                                                        .name(Component.text("Normal"))
                                                        .asGuiItem(e -> {
                                                            storedCommand = "edm wd normal";
                                                            builder.open((Player) sender);
                                                        })
                                                );
                                                waterDamageGui.setItem(4, ItemBuilder.from(Material.RED_STAINED_GLASS_PANE)
                                                        .name(Component.text("Hard"))
                                                        .asGuiItem(e -> {
                                                            storedCommand = "edm wd hard";
                                                            builder.open((Player) sender);
                                                        })
                                                );
                                                waterDamageGui.open((Player) sender);
                                            }
                                    );


                            selectionGui.setItem(0, statusItem);
                            selectionGui.setItem(1, statusItem);
                            selectionGui.setItem(2, ItemBuilder.from(backArrowItemStack)
                                    .asGuiItem(e -> mainGui.open((Player) sender)
                                    )
                            );
                            selectionGui.setItem(3, damageItem);
                            selectionGui.setItem(4, damageItem);
                            selectionGui.open((Player) sender);
                        }
                )

        );
        //Close item
        mainGui.setItem(6, 5, ItemBuilder.from(Material.BARRIER)
                .name(Component.text("Close GUI"))
                .asGuiItem(e -> mainGui.close((HumanEntity) sender))
        );

        mainGui.open((HumanEntity) sender);
    }


    @Override
    public List<String> getName() {
        return List.of("gui");
    }
}
