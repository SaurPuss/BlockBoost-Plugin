package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocklisteners.LandmineListener;
import me.saurpuss.blockboost.blocklisteners.BounceListener;
import me.saurpuss.blockboost.blocklisteners.SpeedListener;
import me.saurpuss.blockboost.util.util.AbstractBlock;
import org.bukkit.Material;

import java.util.*;

public class BBManager {

    private BlockBoost bb;

    // TODO singleton patterns
    private static BounceListener bounceListener;
    private static SpeedListener speedListener;
    private static LandmineListener landmineListener;

    public BBManager(BlockBoost plugin) {
        bb = plugin;

//        validBlocks = blockMaterials();

        HashMap<Material, AbstractBlock> temp = bb.getConfigManager().getBounceBlocks();
        if (temp != null)
            bounceListener = new BounceListener(bb, temp);

//        blocks = new BlockMapper(bb);
        // register event listeners
//        if (blocks.hasVelocityBlocks())
//            bounceListener = new BounceListener(bb, blocks.getBounceBlocks());
//
//        if (blocks.hasSpeedBlocks())
//            speedListener = new SpeedListener(bb, blocks.getSpeedMultiplierBlocks());
//
//        if (blocks.hasLandmineBlocks())
//            landmineListener = new LandmineListener(bb, blocks.getLandmineBlocks());

    }

    public void unloadListeners() {
        if (bounceListener != null) {
            bounceListener.unregister();
            // TODO new bouncelistener etc
        }

        if (speedListener != null)
            speedListener.unregister();

        if (landmineListener != null)
            landmineListener.unregister();

    }
}
