package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.PotionEffectBlock;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.Optional;

public class PotionEffectListener extends AbstractListener implements Listener {

    private final HashMap<Material, AbstractBlock> BLOCKS;

    public PotionEffectListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        // Test validity of the blocks before registering listener
        if (test.isPresent() && test.get() instanceof PotionEffectBlock) {
            BLOCKS = blocks;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else {
            BLOCKS = null;
        }
    }

    @EventHandler
    public void activateBlock(PlayerMoveEvent event) {
        // Check if player is allowed to activate
        final Player player = event.getPlayer();
        if (player.hasPermission("bb.deny"))
            return;

        // Get the block the player is standing on
        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)
            block = block.getRelative(BlockFace.DOWN); // Check in case of a block with < 1.0 height

        // Check for block match
        if (!BLOCKS.containsKey(block.getType())) return;
        final PotionEffectBlock mat = (PotionEffectBlock) BLOCKS.get(block.getType());

        // Check if allowed in this world
        if (!mat.getWorld().equalsIgnoreCase("global")) {
            if ((!mat.getWorld().equalsIgnoreCase(player.getWorld().getName()) && mat.isIncludeWorld())
                    || (mat.getWorld().equalsIgnoreCase(player.getWorld().getName()) && !mat.isIncludeWorld())) {
                return; // current world is disabled
            }
        } else if (mat.getWorld().equalsIgnoreCase("global") && !mat.isIncludeWorld()) {
            return; // all worlds are disabled
        }

        // Apply potion effect
        player.addPotionEffect(new PotionEffect(mat.getEffectType(), mat.getDuration(),
                mat.getAmplifier()));
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
