package me.saurpuss.blockboost.util;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.events.LandmineListener;
import me.saurpuss.blockboost.events.VelocityListener;
import me.saurpuss.blockboost.util.blocks.LandmineBlock;
import me.saurpuss.blockboost.util.blocks.VelocityBlock;

import java.util.HashSet;

public class BBManager {

    private BlockBoost bb;
    private BlockMapper blocks;

    private static VelocityListener velocityListener;
    private static LandmineListener landmineListener;

    public BBManager(BlockBoost plugin) {
        bb = plugin;
        blocks = new BlockMapper(bb);

        // register event listeners
        if (blocks.hasVelocityBlocks())
            velocityListener = new VelocityListener(bb, blocks.getVelocityBlocks());

        if (blocks.hasLandmineBlocks())
            landmineListener = new LandmineListener(bb, blocks.getLandmineBlocks());

    }

    public void unloadListeners() {
        if (velocityListener != null)
            velocityListener.unregister();

        if (landmineListener != null)
            landmineListener.unregister();

    }


}
