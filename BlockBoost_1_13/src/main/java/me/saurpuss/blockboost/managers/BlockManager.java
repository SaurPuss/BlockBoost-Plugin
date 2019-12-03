package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.listeners.BounceListener;
import me.saurpuss.blockboost.listeners.PotionEffectListener;
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
    private final CustomBlockSetup blockSetup;

    private HashSet<BB> temp = new HashSet<>();
    private final HashSet<AbstractListener> activeListeners;

    public volatile HashSet<UUID> playerCooldown = new HashSet<>();

    private final AbstractListener bounceListener, speedMultiplierListener, speedAdditionListener,
            potionEffectListener;

    public BlockManager(BlockBoost plugin) {
        bb = plugin;
        blockSetup = new CustomBlockSetup(plugin);

        // Register all listeners that have valid blocks
        bounceListener = getListener(BB.BOUNCE);
        speedMultiplierListener = getListener(BB.SPEED_MULTIPLIER);
        speedAdditionListener = getListener(BB.SPEED_ADDITION);
        potionEffectListener = getListener(BB.POTION);

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
                case POTION:
                    listeners.add(potionEffectListener);
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
        HashMap<Material, AbstractBlock> blocks = blockSetup.getBlockMap(type);
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
            case POTION:
                return new PotionEffectListener(bb, blocks);
            default:
                return null;
        }
    }

    public List<String> getBlockMap(BB type) {
        ArrayList<String> list = new ArrayList();
        for (AbstractBlock block : blockSetup.getBlockMap(type).values()) {
            list.add("- " + block.toString());
        }

        return list;
    }
}
