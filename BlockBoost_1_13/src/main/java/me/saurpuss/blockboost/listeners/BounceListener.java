package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
import me.saurpuss.blockboost.blocks.BounceBlock;
import org.bukkit.Material;
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

public class BounceListener extends AbstractListener implements Listener {

    private final HashMap<Material, AbstractBlock> BLOCKS;

    public BounceListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        Optional<AbstractBlock> test = blocks.values().stream().findFirst();

        if (test.isPresent() && test.get() instanceof BounceBlock) {
            BLOCKS = blocks;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } else {
            BLOCKS = null;
        }
    }

    @EventHandler
    public void activateBlock(PlayerMoveEvent event) {
        Block block = event.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (!BLOCKS.containsKey(block.getType())) return;

        Player player = event.getPlayer();
        BounceBlock material = (BounceBlock) BLOCKS.get(block.getType());

        // material has an inclusion and it's global or this world
        if (material.isIncludeWorld() &&
                (material.getWorld().equalsIgnoreCase("global") ||
                        material.getWorld().equalsIgnoreCase(player.getWorld().getName()))) {
            triggerVelocity(player, material);
        }
        // material has an exclusion that is not global and is not this world
        else if (!material.isIncludeWorld() &&
                !material.getWorld().equalsIgnoreCase("global") &&
                !material.getWorld().equalsIgnoreCase(player.getWorld().getName())) {
            triggerVelocity(player, material);
        }
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

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
