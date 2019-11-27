package me.saurpuss.blockboost.listeners;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.AbstractBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class BuriedMineListener implements Listener {


    private BlockBoost bb;
    private final HashMap<Material, AbstractBlock> BLOCKS;

    public BuriedMineListener(BlockBoost plugin, HashMap<Material, AbstractBlock> blocks) {
        bb = plugin;
        BLOCKS = blocks;

        bb.getServer().getPluginManager().registerEvents(this, bb);
    }

    @EventHandler
    public void activateVelocityBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block standOn = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        Block standIn = player.getLocation().getBlock();

//        BLOCKS.forEach(material -> {
//            // get buried block
//            Block underBlock = player.getLocation().getBlock().getRelative(0, material.getDepth(), 0);
//
//            // Check if material corresponds with the underBlock
//            if (material.getMaterial() == underBlock.getType()) {
//                if (material.hasCoverBlock()) {
//                    if (material.getCoverBlock() == standOn.getType() ||
//                            material.getCoverBlock() == standIn.getType()) {
//
//
//
//                    }
//                } else {
//
//
////                }
//            }
//        });
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
