package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocklisteners.LandmineListener;
import me.saurpuss.blockboost.blocklisteners.BounceListener;
import me.saurpuss.blockboost.blocklisteners.SpeedListener;
import org.bukkit.Material;

import java.util.HashSet;

public class BBManager {

    private BlockBoost bb;
    private final HashSet<Material> validBlocks;


    private BlockMapper blocks;

    // TODO singleton patterns
    private static BounceListener bounceListener;
    private static SpeedListener speedListener;
    private static LandmineListener landmineListener;

    public BBManager(BlockBoost plugin) {
        bb = plugin;

        validBlocks = blockMaterials();

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

    private HashSet<Material> blockMaterials() {
        HashSet<Material> list = new HashSet<>();
        for (Material mat : Material.values()) {
            if (mat.isBlock()) {
                list.add(mat);
            }
        }

        return list;
    }

    public HashSet<Material> getValidBlocks() {
        return validBlocks;
    }


}
