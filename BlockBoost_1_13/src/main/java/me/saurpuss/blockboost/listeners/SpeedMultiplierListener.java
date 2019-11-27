package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blocks.SpeedMultiplierBlock;
import me.saurpuss.blockboost.util.util.AbstractBlock;
import me.saurpuss.blockboost.util.util.AbstractListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public class SpeedMultiplierListener extends AbstractListener implements Listener {

    private BlockBoost bb;
    private final HashMap<Material, AbstractBlock> BLOCKS;
    private static HashSet<UUID> playerDelay = new HashSet<>();

    public SpeedMultiplierListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        BLOCKS = blocks;

        Optional<AbstractBlock> test = blocks.values().stream().findFirst();
        if (test.isPresent() && test.get() instanceof SpeedMultiplierBlock) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void activateBounceBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        // Event can only fire once a second
        if (!playerDelay.contains(player.getUniqueId())) {
//            BLOCKS.forEach(material -> {
//                if (material.getMaterial() == block.getType()) {
//                    float result = player.getWalkSpeed() * material.g();
//                    if (result > 1.0)
//                        result = 1.0f;
//                    else if (result > material.getSpeedCap())
//                        result = material.getSpeedCap();
//
//                    player.setWalkSpeed(result);
//                    playerDelay.add(player.getUniqueId());
//
//                    // TODO custom task
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () ->
//                            playerDelay.remove(player.getUniqueId()), material.getCooldown());
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () ->
//                        player.setWalkSpeed(material.getDefaultSpeed()), material.getDuration() * 20);
//                }
//            });
        }
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
