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
                ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem());
        trueOrFalseGui.setItem(3, falseItem);
        trueOrFalseGui.setItem(4, falseItem);


        Gui mainGui = Gui.gui()
                .title(Component.text("Endermaning GUI"))
                .rows(6)
                .create();
        mainGui.setDefaultClickAction(e -> e.setCancelled(true));

        ItemStack backArrowItemStack = ItemBuilder.from(Material.ARROW)
                .name(Component.text("Back"))
                .build();

        GuiItem addItem = ItemBuilder.from(Material.NAME_TAG)
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
                                .itemRight(backArrowItemStack)
                                .onRightInputClick(mainGui::open)
                                .title("Name of player to add")
                                .plugin(plugin)
                                .open((Player) sender)
                );

        GuiItem damageKinItem = ItemBuilder.from(Material.IRON_SWORD)
                .name(Component.text("Damage Kin"))
                .asGuiItem(
                        e -> {
                            storedCommand = "edm dmgk";
                            trueOrFalseGui.open((HumanEntity) sender);
                        }
                );

        mainGui.setItem(2, 3, addItem);
        mainGui.setItem(2, 5, damageKinItem);
        mainGui.open((HumanEntity) sender);

        //TODO Complete GUI
        sender.sendMessage(
                Component.text(
                        "Seems like you stumbled onto the gui! It's currently isn't ready yet though. " +
                                "Try using /edm help to get cli instructions for now!"
                )
        );
    }


    @Override
    public List<String> getName() {
        return List.of("gui");
    }
}
