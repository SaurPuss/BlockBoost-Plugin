package me.saurpuss.blockboost.blocklisteners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blockbuilders.SpeedMultiplierBlock;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.UUID;

public class SpeedListener implements Listener {

    private BlockBoost bb;
    private final HashSet<SpeedMultiplierBlock> BLOCKS;
    private static HashSet<UUID> playerDelay = new HashSet<>();

    public SpeedListener(BlockBoost plugin, HashSet<SpeedMultiplierBlock> blocks) {
        bb = plugin;
        BLOCKS = blocks;

        bb.getServer().getPluginManager().registerEvents(this, bb);
    }

    @EventHandler
    public void activateBounceBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        // Event can only fire once a second
        if (!playerDelay.contains(player.getUniqueId())) {
            BLOCKS.forEach(material -> {
                if (material.getMaterial() == block.getType()) {
                    float result = player.getWalkSpeed() * material.getSpeedMultiplier();
                    if (result > 1.0)
                        result = 1.0f;
                    else if (result > material.getSpeedCap())
                        result = material.getSpeedCap();

                    player.setWalkSpeed(result);
                    playerDelay.add(player.getUniqueId());

                    // TODO custom task
                    Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () ->
                            playerDelay.remove(player.getUniqueId()), material.getCooldown());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () ->
                        player.setWalkSpeed(material.getDefaultSpeed()), material.getDuration() * 20);
                }
            });
        }
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
