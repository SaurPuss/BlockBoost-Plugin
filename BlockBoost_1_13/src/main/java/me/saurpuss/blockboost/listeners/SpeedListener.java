package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.SpeedBlock;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
import me.saurpuss.blockboost.util.BBSubType;
import me.saurpuss.blockboost.util.SpeedResetTask;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SpeedListener extends AbstractListener implements Listener {

    private BlockBoost bb;
    private final HashMap<Material, AbstractBlock> BLOCKS;
    private HashMap<UUID, Long> speedBlockCooldown;
    public static volatile HashMap<UUID, Float> speedTasks;

    public SpeedListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        bb = plugin;
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        // Test validity of the blocks
        if (test.isPresent() && test.get() instanceof SpeedBlock) {
            BLOCKS = blocks;
            speedBlockCooldown = new HashMap<>();
            speedTasks = new HashMap<>();
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else
            BLOCKS = null;
    }

    @EventHandler
    public void activateSpeedBlock(PlayerMoveEvent event) {
        // Check if player is allowed to activate
        final Player player = event.getPlayer();

        if (player.hasPermission("bb.deny") || isOnBlockCooldown(player.getUniqueId()))
            return;

        // Get block info & look for match
        Block block = player.getLocation().getBlock();
        if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR)
            block = block.getRelative(BlockFace.DOWN);

        // Check speed block match
        if (!BLOCKS.containsKey(block.getType())) return;
        final SpeedBlock mat = (SpeedBlock) BLOCKS.get(block.getType());

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

    private void triggerSpeed(Player player, SpeedBlock material) {
        final float playerSpeed = player.getWalkSpeed();
        float resultSpeed;

        // Calculate result speed
        if (material.getSubType() == BBSubType.SPEED_MULTIPLIER) {
            resultSpeed = playerSpeed * material.getAmount();
        } else
            resultSpeed = playerSpeed + material.getAmount();

        if (resultSpeed >= 1.0f || resultSpeed > material.getCap())
            resultSpeed = material.getCap();

        player.setWalkSpeed(resultSpeed);
        speedBlockCooldown.put(player.getUniqueId(),
                System.currentTimeMillis() + (material.getCooldown() * 20));

        // Create a reset task to go back to original speed
        if (!isOnSpeedTaskCooldown(player.getUniqueId())) {
            speedTasks.put(player.getUniqueId(), playerSpeed);
            new SpeedResetTask(player, playerSpeed)
                    .runTaskLater(bb, material.getDuration() * 20);
        }
    }

    private boolean isOnBlockCooldown(UUID uuid) {
        if (!speedBlockCooldown.containsKey(uuid))
            return false;

        return speedBlockCooldown.get(uuid) > System.currentTimeMillis();
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        resetSpeedEarly(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        resetSpeedEarly(event.getPlayer());
    }

    public static void finishTask(UUID uuid) {
        speedTasks.remove(uuid);
    }

    private void resetSpeedEarly(Player player) {
        if (speedTasks.containsKey(player.getUniqueId())) {
            player.setWalkSpeed(speedTasks.get(player.getUniqueId()));
            speedTasks.remove(player.getUniqueId());
        }
    }

    public boolean isOnSpeedTaskCooldown(UUID uuid) {
        return speedTasks.containsKey(uuid);
    }
}