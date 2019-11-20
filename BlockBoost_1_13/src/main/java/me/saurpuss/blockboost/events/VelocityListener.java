package me.saurpuss.blockboost.events;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blocks.VelocityBlock;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;

public class VelocityListener implements Listener {

    private BlockBoost bb;
    private final HashSet<VelocityBlock> BLOCKS;

    public VelocityListener(BlockBoost plugin) {
        bb = plugin;
        BLOCKS = bb.getBbManager().getVelocityBlocks();

        bb.getServer().getPluginManager().registerEvents(this, bb);
    }

    @EventHandler
    public void activateVelocityBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        BLOCKS.forEach(material -> {
            if (material.getMaterial() == block.getType()) {
                player.setVelocity(player.getVelocity()
                        .multiply(material.getMultiplier())
                        .setY(material.getHeight()));
            }
        });
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
