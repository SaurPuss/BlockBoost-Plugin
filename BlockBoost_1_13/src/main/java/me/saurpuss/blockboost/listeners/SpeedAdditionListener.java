package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.SpeedAdditionBlock;
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

public class SpeedAdditionListener extends AbstractListener implements Listener {

    private final BlockBoost bb;
    private final HashMap<Material, AbstractBlock> BLOCKS;

    public SpeedAdditionListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        bb = plugin;
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        // Test validity of the blocks before registering listener
        if (test.isPresent() && test.get() instanceof SpeedAdditionBlock) {
            BLOCKS = blocks;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else {
            BLOCKS = null;
        }
    }

    @EventHandler
    public void activateBounceBlock(PlayerMoveEvent event) {
        // Check if player is allowed to activate
        final Player player = event.getPlayer();
        if (player.hasPermission("bb.deny") ||
                // Player has an active speed effect from a BoostBlock
                bb.getBlockManager().speedAdditionCooldown.contains(player.getUniqueId()) ||
                bb.getBlockManager().speedMultiplierCooldown.contains(player.getUniqueId()))
            return;

        // Get block info & look for match
        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)
            block = block.getRelative(BlockFace.DOWN);

        // Check speed block match
        if (!BLOCKS.containsKey(block.getType())) return;
        final SpeedAdditionBlock mat = (SpeedAdditionBlock) BLOCKS.get(block.getType());

        // Check if allowed in this world
        if (!mat.getWorld().equalsIgnoreCase("global")) {
            if ((!mat.getWorld().equalsIgnoreCase(player.getWorld().getName()) && mat.isIncludeWorld())
                    || (mat.getWorld().equalsIgnoreCase(player.getWorld().getName()) && !mat.isIncludeWorld())) {
                return; // this specific world is disabled
            }
        } else if (mat.getWorld().equalsIgnoreCase("global") && !mat.isIncludeWorld()) {
            return; // all worlds are disabled
        }

        // activate
        triggerSpeed(player, mat);
    }

    private void triggerSpeed(Player player, SpeedAdditionBlock material) {
        final float playerSpeed = player.getWalkSpeed();

        float result = playerSpeed + material.getAddition();
        if (result >= 1.0f) result = 1.0f;

        player.setWalkSpeed(result);
        bb.getBlockManager().speedAdditionCooldown.add(player.getUniqueId());

        // TODO custom task
        Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () -> {
            bb.getBlockManager().speedAdditionCooldown.remove(player.getUniqueId());
            player.setWalkSpeed(playerSpeed);
            }, material.getDuration() * 20);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
