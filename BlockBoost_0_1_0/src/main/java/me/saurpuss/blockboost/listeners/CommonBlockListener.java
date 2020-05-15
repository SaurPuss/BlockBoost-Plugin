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

import java.util.HashSet;
import java.util.Set;

public class CommonBlockListener implements Listener {

    private final Set<AbstractBlock> BLOCKS;
    private final HashSet<Material> ignored = new HashSet<>();

    public CommonBlockListener(final BlockBoost plugin, final Set<AbstractBlock> BLOCKS) {
        this.BLOCKS = BLOCKS;

        if (!BLOCKS.isEmpty()) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);

            // Ignore air and cave air by default as a block to check
            ignored.add(Material.AIR);
            ignored.add(Material.CAVE_AIR);

            // ignore carpet, todo config selection instead
            ignored.add(Material.CYAN_CARPET);
            ignored.add(Material.BLACK_CARPET);
            ignored.add(Material.BLUE_CARPET);
            ignored.add(Material.BROWN_CARPET);
            ignored.add(Material.GRAY_CARPET);
            ignored.add(Material.GREEN_CARPET);
            ignored.add(Material.LIGHT_BLUE_CARPET);
            ignored.add(Material.LIGHT_GRAY_CARPET);
            ignored.add(Material.LIME_CARPET);
            ignored.add(Material.MAGENTA_CARPET);
            ignored.add(Material.ORANGE_CARPET);
            ignored.add(Material.PINK_CARPET);
            ignored.add(Material.PURPLE_CARPET);
            ignored.add(Material.RED_CARPET);
            ignored.add(Material.WHITE_CARPET);
            ignored.add(Material.YELLOW_CARPET);
        }
    }

    @EventHandler
    public void activateBlock(final PlayerMoveEvent event) {
        // Check if player is allowed to activate
        final Player player = event.getPlayer();
        if (player.hasPermission("bb.deny"))
            return;

        Material material = player.getLocation().getBlock().getType();
        if (ignored.contains(material))
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
