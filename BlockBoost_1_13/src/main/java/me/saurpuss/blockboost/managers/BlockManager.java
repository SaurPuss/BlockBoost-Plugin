package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.listeners.BounceListener;
import me.saurpuss.blockboost.listeners.PotionEffectListener;
import me.saurpuss.blockboost.listeners.SpeedAdditionListener;
import me.saurpuss.blockboost.listeners.SpeedMultiplierListener;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractListener;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;

import java.util.*;

public class BlockManager {

    private BlockBoost bb;
    private final CustomBlockSetup blockSetup;

    private HashSet<BB> temp = new HashSet<>();

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
    }

    public void unloadListeners() {
        HandlerList.unregisterAll(bb);
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

    public List<String> getBlockList(BB type) {
        if (type == null || blockSetup.getBlockMap(type) == null)
            return new ArrayList<>();

        ArrayList<String> list = new ArrayList<>();
        list.add(ChatColor.GOLD + "" + type + " Blocks:");
        for (AbstractBlock block : blockSetup.getBlockMap(type).values()) {
            list.add("§r- " + block.toColorString());
        }

        return list;
    }
}
