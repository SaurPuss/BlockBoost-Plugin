package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.configs.BounceBlockConfig;
import org.bukkit.configuration.file.FileConfiguration;

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


}
