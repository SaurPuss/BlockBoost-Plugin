package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
import me.saurpuss.blockboost.blocks.BounceBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Optional;

public class BounceListener extends AbstractListener implements Listener {

    private final HashMap<Material, AbstractBlock> BLOCKS;

    public BounceListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        // Test validity of the blocks before registering listener
        if (test.isPresent() && test.get() instanceof BounceBlock) {
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

        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)
            block = block.getRelative(BlockFace.DOWN);

        if (!BLOCKS.containsKey(block.getType())) return;
        final BounceBlock mat = (BounceBlock) BLOCKS.get(block.getType());

        // Check if allowed in this world
        if (!mat.getWorld().equalsIgnoreCase("global")) {
            if ((!mat.getWorld().equalsIgnoreCase(player.getWorld().getName()) && mat.isIncludeWorld())
                    || (mat.getWorld().equalsIgnoreCase(player.getWorld().getName()) && !mat.isIncludeWorld())) {
                return; // this specific world is disabled
            }
        } else if (mat.getWorld().equalsIgnoreCase("global") && !mat.isIncludeWorld()) {
            return; // all worlds are disabled
        }

        triggerVelocity(player, mat);
    }


    private void triggerVelocity(final Player player, final BounceBlock bounceBlock) {
        Vector direction = player.getLocation().getDirection();
        direction.setY(bounceBlock.getHeight());
        direction.setX(player.getVelocity().getX());
        direction.setZ(player.getVelocity().getZ());
        if (bounceBlock.isNormalize())
            direction.normalize();

        player.setVelocity(direction);
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
