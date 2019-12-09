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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SpeedMultiplierListener extends AbstractListener implements Listener {

    private final BlockBoost bb;
    private final HashMap<Material, AbstractBlock> BLOCKS;
    private volatile ArrayList<UUID> multiplierCooldown = new ArrayList<>();

    public SpeedMultiplierListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        bb = plugin;
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        // Test validity of the blocks before registering listener
        if (test.isPresent() && test.get() instanceof SpeedMultiplierBlock) {
            BLOCKS = blocks;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else
            BLOCKS = null;
    }

    @EventHandler
    public void activateSpeedMultiplierBlock(PlayerMoveEvent event) {
        // Check if player is allowed to activate
        final Player player = event.getPlayer();
        if (player.hasPermission("bb.deny") ||
                // player is on cooldown from a SpeedAdditionBlock
                bb.getBlockManager().speedAdditionCooldown.contains(player.getUniqueId()) ||
                // player is on cooldown from Multiplier Block
                multiplierCooldown.contains(player.getUniqueId()))
            return;

        // Get the block the player is standing on
        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)
            block = block.getRelative(BlockFace.DOWN);

        // Check speed block match
        if (!BLOCKS.containsKey(block.getType())) return;
        final SpeedMultiplierBlock mat = (SpeedMultiplierBlock) BLOCKS.get(block.getType());

        // Check if allowed in this world
        if (!mat.getWorld().equalsIgnoreCase("global")) {
            if ((!mat.getWorld().equalsIgnoreCase(player.getWorld().getName()) && mat.isIncludeWorld())
                    || (mat.getWorld().equalsIgnoreCase(player.getWorld().getName()) && !mat.isIncludeWorld())) {
                return; // this specific world is disabled
            }
        } else if (mat.getWorld().equalsIgnoreCase("global") && !mat.isIncludeWorld()) {
            return; // all worlds are disabled
        }

        triggerSpeed(player, mat);
    }

    private void triggerSpeed(Player player, SpeedMultiplierBlock material) {
        float result = player.getWalkSpeed() * material.getSpeedMultiplier();

        if (result > 1.0) result = 1.0f;
        else if (result > material.getSpeedCap()) result = material.getSpeedCap();

        player.setWalkSpeed(result);
        multiplierCooldown.add(player.getUniqueId());
        bb.getBlockManager().speedMultiplierCooldown.add(player.getUniqueId());

        // TODO custom task
        Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () ->
                multiplierCooldown.remove(player.getUniqueId()),
                material.getCooldown());
        Bukkit.getScheduler().scheduleSyncDelayedTask(bb, () -> {
            player.setWalkSpeed(material.getDefaultSpeed());
            bb.getBlockManager().speedMultiplierCooldown.remove(player.getUniqueId());
        }, material.getDuration() * 20);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
