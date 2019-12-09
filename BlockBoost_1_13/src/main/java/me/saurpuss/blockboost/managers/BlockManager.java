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

    private final BlockBoost bb;
    private final CustomBlockSetup blockSetup;

    public volatile HashSet<UUID> playerCooldown = new HashSet<>();

    public BlockManager(BlockBoost plugin) {
        bb = plugin;
        blockSetup = new CustomBlockSetup(plugin);

        // Register all listeners that have valid blocks
        final AbstractListener bounceListener = getListener(BB.BOUNCE);
        final AbstractListener speedMultiplierListener = getListener(BB.SPEED_MULTIPLIER);
        final AbstractListener speedAdditionListener = getListener(BB.SPEED_ADDITION);
        final AbstractListener potionEffectListener = getListener(BB.POTION);
    }

    public void unloadListeners() {
        HandlerList.unregisterAll(bb);
    }

    private AbstractListener getListener(BB type) {
        HashMap<Material, AbstractBlock> blocks = blockSetup.getBlockMap(type);
        if (blocks == null)
            return null;

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
