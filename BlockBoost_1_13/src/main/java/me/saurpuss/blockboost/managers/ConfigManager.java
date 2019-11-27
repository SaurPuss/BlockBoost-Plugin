package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.configs.BounceBlockConfig;
import me.saurpuss.blockboost.util.util.AbstractBlock;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class ConfigManager {

    private BlockBoost bb;
    private BounceBlockConfig bounceBlockConfig;

    public ConfigManager(BlockBoost plugin) {
        bb = plugin;
        loadBounceBlockConfig();
    }

    private void loadBounceBlockConfig() {
        bounceBlockConfig = new BounceBlockConfig(bb);
        bounceBlockConfig.setup();


    }

    FileConfiguration getBounceBlockConfig() {
        return bounceBlockConfig.get();
    }

    HashMap<Material, AbstractBlock> getBounceBlocks() {
        return bounceBlockConfig.getBlockMap();
    }




}
