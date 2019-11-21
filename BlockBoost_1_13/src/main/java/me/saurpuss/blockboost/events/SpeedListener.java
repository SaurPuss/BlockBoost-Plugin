package me.saurpuss.blockboost.events;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blocks.SpeedBlock;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;

public class SpeedListener implements Listener {

    private BlockBoost bb;
    private final HashSet<SpeedBlock> BLOCKS;
    final float defaultSpeed = 0.2f;

    public SpeedListener(BlockBoost plugin, HashSet<SpeedBlock> blocks) {
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
                float result = player.getWalkSpeed() * material.getSpeedMultiplier();
                if (result >= 1)
                    result = 1f;

                player.setWalkSpeed(result);
                player.sendMessage("Speed set to " + material.getSpeedMultiplier());

                // TODO custom task
                Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () -> {
                    player.sendMessage("Speed set back to " + defaultSpeed);
                    player.setWalkSpeed(defaultSpeed);
                        }, 200L);
            }
        });
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
