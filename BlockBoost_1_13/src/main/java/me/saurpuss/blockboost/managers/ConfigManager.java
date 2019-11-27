package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.configs.BounceBlockConfig;
import me.saurpuss.blockboost.util.configs.SpeedBlockConfig;
import me.saurpuss.blockboost.util.util.AbstractBlock;
import me.saurpuss.blockboost.util.util.BB;
import me.saurpuss.blockboost.util.util.CustomConfig;
import org.bukkit.Material;

import java.util.HashMap;

public class ConfigManager {

    private BlockBoost bb;

    private final CustomConfig bounceBlockConfig, speedblockConfig;

    public ConfigManager(BlockBoost plugin) {
        bb = plugin;

        bounceBlockConfig = loadCustomConfig(BB.BOUNCE);
        speedblockConfig = loadCustomConfig(BB.SPEED_MULTIPLIER);


        setup();
    }

    private CustomConfig loadCustomConfig(BB type) {
        switch (type) {
            case BOUNCE:
                return new BounceBlockConfig(bb);
            case SPEED_MULTIPLIER:
                return new SpeedBlockConfig(bb);
            default:
                return null;
        }
    }

    private void setup() {
        if (bounceBlockConfig != null)
            bounceBlockConfig.setup();
    }

    HashMap<Material, AbstractBlock> getBounceBlocks() {
        return bounceBlockConfig.getBlockMap();
    }

    HashMap<Material, AbstractBlock> getSpeedBlocks() {
        return speedblockConfig.getBlockMap();
    }




}
