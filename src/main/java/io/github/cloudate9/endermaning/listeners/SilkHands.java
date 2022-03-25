package io.github.cloudate9.endermaning.listeners;

import io.github.cloudate9.endermaning.config.HybridConfig;
import io.github.cloudate9.endermaning.config.OptionsConfig;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

@SuppressWarnings("ClassCanBeRecord")
public class SilkHands implements Listener {

    private final HybridConfig hybridConfig;
    private final OptionsConfig optionsConfig;

    @Inject
    public SilkHands(HybridConfig hybridConfig, OptionsConfig optionsConfig) {
        this.hybridConfig = hybridConfig;
        this.optionsConfig = optionsConfig;
    }

    @EventHandler
    public void playerEndermanHandBreak(BlockBreakEvent e) {
        if (!optionsConfig.hybridHasSilkTouchHands) return;
        if (!hybridConfig.hybridList.contains(e.getPlayer().getUniqueId().toString())) return;
        if (e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) return;

        switch (e.getBlock().getType()) {
            //Try to prevent duping.
            /*
            If foot of bed is broken, there will be two beds dropped,
            as the foot is cancelled but the bed drops from head.
            Can safely return as beds drop without silk touch anyway.
             */
            case BLUE_BED, RED_BED, CYAN_BED, GRAY_BED, LIME_BED,
                    BLACK_BED, BROWN_BED, PINK_BED, GREEN_BED,
                    WHITE_BED, ORANGE_BED, PURPLE_BED, YELLOW_BED,
                    MAGENTA_BED, LIGHT_BLUE_BED, LIGHT_GRAY_BED, CAKE -> {
                return;
            }
        }

        try {
            e.getPlayer().getWorld()
                    .dropItemNaturally(e.getBlock().getLocation(), new ItemStack(e.getBlock().getType())); //Drop block.
            e.setCancelled(true); //Only cancel after block is dropped, so only blocks don't disappear if failed.
            e.setExpToDrop(0); //Unclear if this is unnecessary, but it's here jic.
            e.getBlock().setType(Material.AIR);
        } catch (IllegalArgumentException ignored) {
            /*
            Do nothing. Just stop console output.
            Things like carrot and potato crops will cause this, as the "carrot crop" is not a block.
            The appropriate block will still be dropped, as we have not cancelled the event yet.
             */
        }
    }
}
