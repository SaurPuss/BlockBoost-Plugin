package me.saurpuss.blockboost.events;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blocks.BounceBlock;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;

public class BounceListener implements Listener {

    private BlockBoost bb;
    private final HashSet<BounceBlock> BLOCKS;

    public BounceListener(BlockBoost plugin, HashSet<BounceBlock> blocks) {
        bb = plugin;
        BLOCKS = blocks;

        bb.getServer().getPluginManager().registerEvents(this, bb);
    }

    @EventHandler
    public void activateBounceBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        BLOCKS.forEach(material -> {
            if (material.getMaterial() == block.getType()) {
                Vector direction = player.getLocation().getDirection();
                direction.setY(material.getHeight());
                direction.setX(player.getVelocity().getX());
                direction.setZ(player.getVelocity().getZ());
                if (material.isNormalize())
                    direction.normalize();

                player.setVelocity(direction);
            }
        });
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
