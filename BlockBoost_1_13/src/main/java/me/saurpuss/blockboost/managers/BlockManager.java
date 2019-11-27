package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.listeners.BounceListener;
import me.saurpuss.blockboost.listeners.SpeedAdditionListener;
import me.saurpuss.blockboost.listeners.SpeedMultiplierListener;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.Material;

import java.util.*;
import java.util.logging.Level;

public class BlockManager {

    private BlockBoost bb;

    private final HashSet<AbstractListener> activeListeners;
    public HashSet<UUID> playerCooldown = new HashSet<>();

    // TODO is this more efficient than reflection?
    private transient HashSet<BB> temp = new HashSet<>();

    private final AbstractListener bounceListener, speedMultiplierListener, speedAdditionListener,
            landmineListener;

    public BlockManager(BlockBoost plugin) {
        bb = plugin;

        // Register all listeners that have valid blocks
        bounceListener = getListener(BB.BOUNCE);
        speedMultiplierListener = getListener(BB.SPEED_MULTIPLIER);
        speedAdditionListener = getListener(BB.SPEED_ADDITION);
        landmineListener = getListener(BB.LANDMINE);

        // Make sure this happens last!
        activeListeners = listActiveListeners();
    }

    private HashSet<AbstractListener> listActiveListeners() {
        HashSet<AbstractListener> listeners = new HashSet<>();
        temp.forEach(bb -> {
            switch (bb) {
                case BOUNCE:
                    listeners.add(bounceListener);
                    break;
                case SPEED_MULTIPLIER:
                    listeners.add(speedMultiplierListener);
                    break;
                case SPEED_ADDITION:
                    listeners.add(speedAdditionListener);
                    break;
                case LANDMINE:
                    listeners.add(landmineListener);
                    break;

                default: // skip
                    this.bb.getLogger().log(Level.WARNING, "Illegal BB found in temp active " +
                            "listener tracker! Skipping " + bb.name());
            }
        });

        return listeners;
    }

    public void unloadListeners() {
        activeListeners.forEach(listener -> {
            listener.unregister();
            bb.getLogger().log(Level.INFO, "Unregistered " + listener.toString());
        });
    }

    private AbstractListener getListener(BB type) {
        HashMap<Material, AbstractBlock> blocks = bb.getConfigManager().getBlocks(type);
        if (blocks == null)
            return null;

        temp.add(type);

        switch (type) {
            case BOUNCE:
                return new BounceListener(bb, blocks);
            case SPEED_MULTIPLIER:
                return new SpeedMultiplierListener(bb, blocks);
            case SPEED_ADDITION:
                return new SpeedAdditionListener(bb, blocks);
            default:
                return null;
        }
    }
}
