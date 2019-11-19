package me.saurpuss.blockboost.events;

import me.saurpuss.blockboost.BlockBoost;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class VelocityBlockListener implements Listener {

    private BlockBoost bb;

    public VelocityBlockListener(BlockBoost plugin) {
        bb = plugin;
        bb.getServer().getPluginManager().registerEvents(this, bb);


    }

    @EventHandler
    public void activateVelocityBlock(PlayerMoveEvent event) {


    }

//    @EventHandler(priority = EventPriority.LOW)
//    public void activateBlockEffect(PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//        Location location = player.getLocation();
//        Block block = location.getBlock().getRelative(BlockFace.DOWN);
////        Block blockUnder = location.getBlock().getRelative(BlockFace.DOWN, offset);
//    }
//
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
