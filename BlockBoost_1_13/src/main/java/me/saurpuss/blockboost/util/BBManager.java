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

    private VelocityListener velocityListener = null;
    private LandmineListener landmineListener = null;

    public BBManager(BlockBoost plugin) {
        bb = plugin;
        blocks = new BlockMapper(bb);

        // register event listeners
        if (blocks.hasVelocityBlocks())
            velocityListener = new VelocityListener(bb);

        if (blocks.hasLandmineBlocks())
            landmineListener = new LandmineListener(bb);

    }

    public void unloadListeners() {
        if (velocityListener != null)
            velocityListener.unregister();

        if (landmineListener != null)
            landmineListener.unregister();

    }

    public HashSet<VelocityBlock> getVelocityBlocks() {
        return blocks.getVelocityBlocks();
    }

    public HashSet<LandmineBlock> getLandmineBlocks() {
        return blocks.getLandmineBlocks();
    }


}
