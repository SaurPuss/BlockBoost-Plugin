package me.saurpuss.blockboost.util;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.events.LandmineListener;
import me.saurpuss.blockboost.events.BounceListener;
import me.saurpuss.blockboost.events.SpeedListener;

public class BBManager {

    private BlockBoost bb;
    private BlockMapper blocks;

    private static BounceListener bounceListener;
    private static SpeedListener speedListener;
    private static LandmineListener landmineListener;

    public BBManager(BlockBoost plugin) {
        bb = plugin;
        blocks = new BlockMapper(bb);

        // register event listeners
        if (blocks.hasVelocityBlocks())
            bounceListener = new BounceListener(bb, blocks.getBounceBlocks());

        if (blocks.hasSpeedBlocks())
            speedListener = new SpeedListener(bb, blocks.getSpeedBlocks());

        if (blocks.hasLandmineBlocks())
            landmineListener = new LandmineListener(bb, blocks.getLandmineBlocks());

    }

    public void unloadListeners() {
        if (bounceListener != null)
            bounceListener.unregister();

        if (speedListener != null)
            speedListener.unregister();

        if (landmineListener != null)
            landmineListener.unregister();

    }


}
