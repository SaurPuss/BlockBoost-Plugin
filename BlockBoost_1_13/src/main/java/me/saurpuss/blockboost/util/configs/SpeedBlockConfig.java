package me.saurpuss.blockboost.util.configs;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blocks.BounceBlock;
import me.saurpuss.blockboost.util.util.AbstractBlock;
import me.saurpuss.blockboost.util.util.CustomConfig;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public class SpeedBlockConfig extends CustomConfig {

    private BlockBoost bb;

    private File file; // speedBlocks.yml location
    private FileConfiguration customFile;

    private HashMap<Material, AbstractBlock> blockMap;

    public SpeedBlockConfig(BlockBoost plugin) {
        bb = plugin;
    }

    @Override
    public void setup() {
        if (!bb.getDataFolder().exists())
            bb.getDataFolder().mkdirs();

        file = new File(bb.getDataFolder(), "speedBlocks.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                bb.getLogger().log(Level.SEVERE, "Could not create speedBlocks.yml!");
                blockMap = null;
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        bb.getLogger().log(Level.INFO, "speedBlocks.yml exists in plugin directory!");

        save();

        if (file.length() == 0 || customFile.getConfigurationSection("blocks") == null) {
            bb.saveResource("speedBlocks.yml", true);
            bb.getLogger().log(Level.WARNING, "Invalid speedBlocks.yml, copying default " +
                    "configuration!");
        }

        if (hasValidKeys()) {
            bb.getLogger().log(Level.INFO, "Reading valid SpeedBlocks!");
            blockMap = populateBlockMap();
        } else
            blockMap = null;
    }

    @Override
    public void reload() {
        if (customFile == null) {
            file = new File(bb.getDataFolder(), "speedBlocks.yml");
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        bb.getLogger().log(Level.INFO, "speedBlocks.yml has been reloaded!");
    }

    @Override
    public FileConfiguration get() {
        return customFile;
    }


    @Override
    public void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            bb.getLogger().log(Level.SEVERE, "Could not save speedBlocks.yml!");
        }
    }


    @Override
    protected boolean hasValidKeys() {

        // TODO refactor for both types of speed blocks
        ConfigurationSection section = customFile.getConfigurationSection("blocks");
        if (section == null) {
            bb.getLogger().log(Level.WARNING, "No valid path found in speedBlocks.yml! " +
                    "Ignoring SpeedBlocks!");
            return false;
        }

        Set<String> keys = section.getKeys(false);
        if (!customFile.isConfigurationSection("blocks") || keys.isEmpty()) {
            bb.getLogger().log(Level.WARNING, "No blocks found in speedBlocks.yml! " +
                    "Ignoring SpeedBlocks!");
            return false;
        }

        if (keys.contains("EXAMPLE_BLOCK")) {
            bb.getLogger().log(Level.INFO, "EXAMPLE_BLOCK found in speedBlocks.yml!");
        }

        return true;
    }

    @Override
    protected HashMap<Material, AbstractBlock> populateBlockMap() {
        ConfigurationSection section = customFile.getConfigurationSection("blocks");
        Set<String> keys = section.getKeys(false);

        HashMap<Material, AbstractBlock> validMats = new HashMap<>();
        keys.forEach(key -> {
//            if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
                Material material = Material.getMaterial(key.toUpperCase());
                if (material == null || !material.isBlock()) {
                    bb.getLogger().log(Level.WARNING,
                            "Material " + key + " speedBlocks.yml is invalid! " +
                                    "Ignoring " + key + "!");
                } else {
                    String world = section.getString(key + ".world");
                    boolean include = section.getBoolean(key + ".include-world");
                    int height = section.getInt(key + ".height");
                    boolean normalize = section.getBoolean(key + ".normalize");

                    AbstractBlock block = new BounceBlock.Builder(material)
                            .withWorld(world).withIncludeWorld(include)
                            .withHeight(height).withNormalize(normalize).build();

                    bb.getLogger().log(Level.INFO, "SpeedBlock added: " + block.toString());
                    validMats.put(material, block);
                }
//            }
        });

        if (validMats.size() == 0) {
            bb.getLogger().log(Level.WARNING, "No valid entries in speed configuration " +
                    "section found!");
            return null;
        }

        return validMats;
    }

    @Override
    public HashMap<Material, AbstractBlock> getBlockMap() {
        return blockMap;
    }
}
