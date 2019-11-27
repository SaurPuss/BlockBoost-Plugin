package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.SpeedMultiplierBlock;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Optional;

public class SpeedMultiplierListener extends AbstractListener implements Listener {

    private BlockBoost bb;
    private final HashMap<Material, AbstractBlock> BLOCKS;

    public SpeedMultiplierListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        bb = plugin;
        BLOCKS = blocks;

        // test first block in hash map before registering the listener
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();
        if (test.isPresent() && test.get() instanceof SpeedMultiplierBlock) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void activateBounceBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        String world = player.getWorld().getName();

        if (!bb.getBlockManager().playerCooldown.contains(player.getUniqueId())) {
            BLOCKS.forEach((key, mat) -> {
                if (key == block.getType()) {
                    SpeedMultiplierBlock material = (SpeedMultiplierBlock) mat;
                    if (material.isIncludeWorld() &&
                            (material.getWorld().equalsIgnoreCase("global") ||
                                    material.getWorld().equalsIgnoreCase(world))) {
                        triggerSpeed(player, material);
                    }
                    // material has an exclusion that is not global and is not this world
                    else if (!material.isIncludeWorld() &&
                            !material.getWorld().equalsIgnoreCase("global") &&
                            !material.getWorld().equalsIgnoreCase(world)) {
                        triggerSpeed(player, material);
                    }
                }
            });
        }
    }

    private void triggerSpeed(Player player, SpeedMultiplierBlock material) {
        float result = player.getWalkSpeed() * material.getSpeedMultiplier();

        if (result > 1.0)
            result = 1.0f;
        else if (result > material.getSpeedCap())
            result = material.getSpeedCap();

        player.setWalkSpeed(result);
        bb.getBlockManager().playerCooldown.add(player.getUniqueId());

        // TODO custom task
        Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () ->
                bb.getBlockManager().playerCooldown.remove(player.getUniqueId()),
                material.getCooldown());
        Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () ->
                player.setWalkSpeed(material.getDefaultSpeed()), material.getDuration() * 20);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
