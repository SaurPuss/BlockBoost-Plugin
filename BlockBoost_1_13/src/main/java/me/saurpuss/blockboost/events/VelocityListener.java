package me.saurpuss.blockboost.events;

import me.saurpuss.blockboost.BlockBoost;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class VelocityListener implements Listener {

    private BlockBoost bb;
    private final HashMap<Material, int[]> BLOCKS;

    public VelocityListener(BlockBoost plugin) {
        bb = plugin;
        BLOCKS = bb.getBbManager().getVelocityMap();

        bb.getServer().getPluginManager().registerEvents(this, bb);
    }

    @EventHandler
    public void activateVelocityBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);

        BLOCKS.forEach((material, ints) -> {
            if (material == block.getType()) {
                player.setVelocity(player.getVelocity().multiply(ints[0]).setY(ints[1]));
            }
        });
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
