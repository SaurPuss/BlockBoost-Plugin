package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.configs.BounceBlockConfig;
import me.saurpuss.blockboost.util.configs.SpeedBlockConfig;
import me.saurpuss.blockboost.util.util.AbstractBlock;
import me.saurpuss.blockboost.util.util.BB;
import me.saurpuss.blockboost.util.util.AbstractConfig;
import org.bukkit.Material;

import java.util.HashMap;

public class ConfigManager {

    private BlockBoost bb;

    private final AbstractConfig bounceBlockConfig, speedBlockConfig;

    public ConfigManager(BlockBoost plugin) {
        bb = plugin;

        bounceBlockConfig = loadCustomConfig(BB.BOUNCE);
        speedBlockConfig = loadCustomConfig(BB.SPEED);


        setup();
    }

    private AbstractConfig loadCustomConfig(BB type) {
        switch (type) {
            case BOUNCE:
                return new BounceBlockConfig(bb);
            case SPEED:
                return new SpeedBlockConfig(bb);
            default:
                return null;
        }
    }

    private void setup() {
        if (bounceBlockConfig != null)
            bounceBlockConfig.setup();
    }



    HashMap<Material, AbstractBlock> getBlocks(BB type) {
        switch (type) {
            case BOUNCE:
                return bounceBlockConfig.getBlockMap(type);
            case SPEED_MULTIPLIER:
            case SPEED_ADDITION:
                return speedBlockConfig.getBlockMap(type);

            default:
                return null;
        }
    }




}
