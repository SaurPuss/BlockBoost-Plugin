package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.AbstractBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Set;

public class CommonBlockListener implements Listener {

    private final Set<AbstractBlock> BLOCKS;

    public CommonBlockListener(final BlockBoost plugin, final Set<AbstractBlock> BLOCKS) {
        this.BLOCKS = BLOCKS;
        if (!BLOCKS.isEmpty())
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void activateBlock(final PlayerMoveEvent event) {
        // Check if player is allowed to activate
        final Player player = event.getPlayer();
        if (player.hasPermission("bb.deny"))
            return;

        Material material = player.getLocation().getBlock().getType();
        if (material == Material.AIR || material == Material.CAVE_AIR)
            material = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();

        iterate(material, player);
    }

    private void iterate(final Material material, final Player player) {
        for (AbstractBlock block : BLOCKS) {
            if (block.getMaterial() == material) {
                if (block.isIncludeWorld()) { // whitelist check
                    if (block.getWorld().equalsIgnoreCase("global") ||
                            block.getWorld().equalsIgnoreCase(player.getWorld().getName()))
                        block.activate(player);
                } else { // blacklist check TODO double check && vs ||
                    if (!block.getWorld().equalsIgnoreCase("global") &&
                            !block.getWorld().equalsIgnoreCase(player.getWorld().getName()))
                        block.activate(player);
                }
            }
        }
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
