package me.saurpuss.blockboost.util;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.events.VelocityListener;
import org.bukkit.Material;

import java.util.HashMap;

public class BBManager {

    private BlockBoost bb;
    private BlockMapper blocks;

    private VelocityListener velocityListener = null;

    public BBManager(BlockBoost plugin) {
        bb = plugin;
        blocks = new BlockMapper(bb.getConfig());

        // register event listeners
        if (blocks.hasVelocityBlocks())
            velocityListener = new VelocityListener(bb);


    }

    public void unloadListeners() {
        if (velocityListener != null)
            velocityListener.unregister();


    }


    public HashMap<Material, int[]> getVelocityMap() {
        return blocks.getVelocityBlocks();
    }
}
