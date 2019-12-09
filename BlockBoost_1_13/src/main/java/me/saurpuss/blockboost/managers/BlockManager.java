package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.listeners.*;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.*;

public class BlockManager {

    private final BlockBoost bb;
    private final CustomBlockSetup blockSetup;

    public BlockManager(BlockBoost plugin) {
        bb = plugin;
        blockSetup = new CustomBlockSetup(plugin);

        // Register PlayerListener
        new PlayerListener(bb);

        // Register all valid block listeners
        setListener(BB.BOUNCE);
        setListener(BB.SPEED_MULTIPLIER);
        setListener(BB.SPEED_ADDITION);
        setListener(BB.POTION);
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
