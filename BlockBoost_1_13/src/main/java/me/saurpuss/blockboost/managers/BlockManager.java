package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.listeners.BounceListener;
import me.saurpuss.blockboost.listeners.SpeedListener;
import me.saurpuss.blockboost.util.util.AbstractBlock;
import me.saurpuss.blockboost.util.util.AbstractListener;
import me.saurpuss.blockboost.util.util.BB;
import org.bukkit.Material;

import java.util.*;

public class BlockManager {

    private BlockBoost bb;

    private final AbstractListener bounceListener, speedListener, landmineListener;

    public BlockManager(BlockBoost plugin) {
        bb = plugin;

        bounceListener = getListener(BB.BOUNCE);
        speedListener = getListener(BB.SPEED_MULTIPLIER);
        landmineListener = getListener(BB.LANDMINE);



    }

    public void unloadListeners() {
        if (bounceListener != null)
            bounceListener.unregister();

        if (speedListener != null)
            speedListener.unregister();

        if (landmineListener != null)
            landmineListener.unregister();

    }

    private AbstractListener getListener(BB type) {
        HashMap<Material, AbstractBlock> temp;

        switch (type) {
            case BOUNCE:
                temp = bb.getConfigManager().getBounceBlocks();
                if (temp != null)
                    return new BounceListener(bb, temp);
            case SPEED_MULTIPLIER:
                temp = bb.getConfigManager().getSpeedBlocks();
                if (temp != null)
                    return new SpeedListener(bb, temp);
            default:
                return null;
        }
    }
}
