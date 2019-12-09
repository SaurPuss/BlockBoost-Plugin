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

    // Cooldown checks to prevent multiple speedBlock types from overriding player walkSpeed
    public volatile ArrayList<UUID> speedAdditionCooldown = new ArrayList<>();
    public volatile ArrayList<UUID> speedMultiplierCooldown = new ArrayList<>();

    public BlockManager(BlockBoost plugin) {
        bb = plugin;
        blockSetup = new CustomBlockSetup(plugin);

        // Register all listeners that have valid blocks
        setListener(BB.BOUNCE);
        setListener(BB.SPEED_MULTIPLIER);
        setListener(BB.SPEED_ADDITION);
        setListener(BB.POTION);
    }

    public void unloadListeners() {
        HandlerList.unregisterAll(bb);
    }

    private void setListener(BB type) {
        HashMap<Material, AbstractBlock> blocks = blockSetup.getBlockMap(type);
        if (blocks == null) return;

        switch (type) {
            case BOUNCE:
                new BounceListener(bb, blocks);
            case SPEED_MULTIPLIER:
                new SpeedMultiplierListener(bb, blocks);
            case SPEED_ADDITION:
                new SpeedAdditionListener(bb, blocks);
            case POTION:
                new PotionEffectListener(bb, blocks);
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
