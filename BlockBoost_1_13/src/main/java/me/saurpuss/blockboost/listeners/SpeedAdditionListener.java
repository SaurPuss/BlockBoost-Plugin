package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.SpeedAdditionBlock;
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
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

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
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        String world = player.getWorld().getName();

        // Event can only fire once a second
        if (!bb.getBlockManager().playerCooldown.contains(player.getUniqueId())) {
            BLOCKS.forEach((key, mat) -> {
                if (key == block.getType()) {
                    SpeedAdditionBlock material = (SpeedAdditionBlock) mat;
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

    private void triggerSpeed(Player player, SpeedAdditionBlock material) {
        float playerSpeed = player.getWalkSpeed();

        float result = playerSpeed + material.getAddition();
        if (result > 1.0)
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
