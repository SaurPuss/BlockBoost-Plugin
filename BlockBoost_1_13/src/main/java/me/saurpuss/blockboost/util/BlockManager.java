package me.saurpuss.blockboost.util;

import me.saurpuss.blockboost.BlockBoost;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class BlockManager {

    private BlockBoost bb;
    private FileConfiguration config;

    private final HashMap<Material, int[]> velocityBlocks;


    public BlockManager(BlockBoost plugin) {
        bb = plugin;
        config = bb.getConfig();

        velocityBlocks = populateVelocityBlocks();

    }


    private HashMap<Material, int[]> populateVelocityBlocks() {
        if (config.getConfigurationSection("velocity-blocks").getKeys(false).size() == 0)
            return null;

        HashMap<Material, int[]> validMats = new HashMap<>();
        config.getConfigurationSection(
                "block-boost.velocity-blocks").getKeys(false).forEach(key -> {
            Material material = Material.getMaterial(key);
            int[] numbers = new int[2];
            if (material != null) {
                numbers[0] = config.getInt("block-boost.velocity." + key + ".multiplier");
                numbers[1] = config.getInt("block-boost.velocity." + key + ".height");
                validMats.put(material, numbers);
                // TODO logger
            } else {
                // TODO warning
            }
        });

        if (validMats.size() == 0)
            return null;

        return validMats;
    }
}
