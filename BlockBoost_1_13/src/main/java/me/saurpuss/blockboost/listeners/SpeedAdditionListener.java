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

    private BlockBoost bb;
    private final HashMap<Material, AbstractBlock> BLOCKS;

    public SpeedAdditionListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        bb = plugin;
        BLOCKS = blocks;

        Optional<AbstractBlock> test = blocks.values().stream().findFirst();
        if (test.isPresent() && test.get() instanceof SpeedAdditionBlock) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void activateBounceBlock(PlayerMoveEvent event) {
        // Check if player is allowed to activate
        if (event.getPlayer().hasPermission("bb.deny") ||
                bb.getBlockManager().playerCooldown.contains(event.getPlayer().getUniqueId()))
            return;

        // Get block info & look for match
        Block block = event.getPlayer().getLocation().getBlock();
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)
            block = block.getRelative(BlockFace.DOWN);

        // Check speed block match
        if (!BLOCKS.containsKey(block.getType()))
            return;

        Player player = event.getPlayer();
        SpeedAdditionBlock material = (SpeedAdditionBlock) BLOCKS.get(block.getType());

        // Check if allowed in this world
        if (!material.getWorld().equalsIgnoreCase("global")) {
            if ((!material.getWorld().equalsIgnoreCase(player.getWorld().getName()) && material.isIncludeWorld())
                    || (material.getWorld().equalsIgnoreCase(player.getWorld().getName()) && !material.isIncludeWorld())) {
                return; // this specific world is disabled
            }
        } else if (material.getWorld().equalsIgnoreCase("global") && !material.isIncludeWorld()) {
            return; // all worlds are disabled
        }

        // activate
        triggerSpeed(player, material);
    }

    private void triggerSpeed(Player player, SpeedAdditionBlock material) {
        final float playerSpeed = player.getWalkSpeed();

        float result = playerSpeed + material.getAddition();
        if (result >= 1.0f)
            result = 1.0f;

        player.setWalkSpeed(result);
        bb.getBlockManager().playerCooldown.add(player.getUniqueId());

        // TODO custom task
        Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () -> {
            bb.getBlockManager().playerCooldown.remove(player.getUniqueId());
            player.setWalkSpeed(playerSpeed);
            }, material.getDuration() * 20);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
