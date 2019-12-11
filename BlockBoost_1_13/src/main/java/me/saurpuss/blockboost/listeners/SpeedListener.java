package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.SpeedBlock;
import me.saurpuss.blockboost.managers.BlockManager;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BBSubType;
import me.saurpuss.blockboost.util.SpeedResetTask;
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
import java.util.UUID;

public class SpeedListener implements Listener {

    private BlockBoost bb;
    private final HashMap<Material, AbstractBlock> BLOCKS;
    private HashMap<UUID, Long> cooldown;

    public SpeedListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        bb = plugin;
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        // Test validity of the blocks
        if (test.isPresent() && test.get() instanceof SpeedBlock) {
            BLOCKS = blocks;
            cooldown = new HashMap<>();
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else
            BLOCKS = null;
    }

    @EventHandler
    public void activateSpeedBlock(PlayerMoveEvent event) {
        // Check if player is allowed to activate
        final Player player = event.getPlayer();
        if (player.hasPermission("bb.deny") || isOnBlockCooldown(player.getUniqueId())) return;

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
        if (material.getType() == BBSubType.SPEED_MULTIPLIER) {
            resultSpeed = playerSpeed * material.getAmount();
        } else
            resultSpeed = playerSpeed + material.getAmount();

        if (resultSpeed >= 1.0f || resultSpeed > material.getCap())
            resultSpeed = material.getCap();

        player.sendMessage("activating speedblock of type " + material.getType());
        player.setWalkSpeed(resultSpeed);
        cooldown.put(player.getUniqueId(),
                System.currentTimeMillis() + (material.getCooldown() * 20));

        // Create a reset task to go back to original speed
        if (!BlockManager.isOnSpeedTaskCooldown(player.getUniqueId())) {
            BlockManager.speedTaskCooldown.put(player.getUniqueId(),
                    System.currentTimeMillis() + (material.getDuration() * 1000));

            player.sendMessage("adding speed task");
            new SpeedResetTask(player, playerSpeed).runTaskLater(bb, material.getDuration() * 20);
        }
    }

    private boolean isOnBlockCooldown(UUID uuid) {
        return cooldown.get(uuid) > System.currentTimeMillis();
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
