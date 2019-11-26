package me.saurpuss.blockboost.blocklisteners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blockbuilders.BounceBlock;
import me.saurpuss.blockboost.util.util.AbstractBlock;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Optional;

public class BounceListener implements Listener {

    private final HashMap<Material, AbstractBlock> BLOCKS;

    public BounceListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        BLOCKS = blocks;

        // Test for valid block type
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();
        if (test.isPresent() && test.get() instanceof BounceBlock) {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void activateBounceBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        World world = player.getWorld();

        BLOCKS.forEach((key, mat) -> {
            if (key == block.getType()) {
                BounceBlock material = (BounceBlock) mat;
                // material has an inclusion and it's global or this world
                if (material.isIncludeWorld() &&
                        (material.getWorld().equalsIgnoreCase("global") ||
                         material.getWorld().equalsIgnoreCase(world.toString()))) {
                    triggerVelocity(player, material);
                }
                // material has an exclusion that is not global and is not this world
                else if (!material.isIncludeWorld() &&
                        !material.getWorld().equalsIgnoreCase("global") &&
                        !material.getWorld().equalsIgnoreCase(world.toString())) {
                    triggerVelocity(player, material);
                }
            }
        });
    }

    private void triggerVelocity(Player player, BounceBlock bounceBlock) {
        Vector direction = player.getLocation().getDirection();
        direction.setY(bounceBlock.getHeight());
        direction.setX(player.getVelocity().getX());
        direction.setZ(player.getVelocity().getZ());
        if (bounceBlock.isNormalize())
            direction.normalize();

        player.setVelocity(direction);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
