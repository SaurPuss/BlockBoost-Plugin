package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocklisteners.LandmineListener;
import me.saurpuss.blockboost.blocklisteners.BounceListener;
import me.saurpuss.blockboost.blocklisteners.SpeedListener;

public class BBManager {

    private BlockBoost bb;
    private BlockMapper blocks;

    // TODO singleton patterns
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
            speedListener = new SpeedListener(bb, blocks.getSpeedMultiplierBlocks());

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
