package me.saurpuss.blockboost.util.configs;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blockbuilders.BounceBlock;
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

public class BounceBlockConfig extends CustomConfig {

    private BlockBoost bb;

    private File file; // bounceBlocks.yml location
    private FileConfiguration customFile;

    private HashMap<Material, AbstractBlock> blockMap;

    public BounceBlockConfig(BlockBoost plugin) {
        bb = plugin;
    }

    @Override
    public void setup() {
        if (!bb.getDataFolder().exists())
            bb.getDataFolder().mkdirs();

        file = new File(bb.getDataFolder(), "bounceBlocks.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                bb.getLogger().log(Level.WARNING, "Could not create bounceBlocks.yml!");
                blockMap = null;
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        bb.getLogger().log(Level.INFO, "bounceBlocks.yml created");

        blockMap = populateBlockMap();
    }

    @Override
    public void reload() {
        if (customFile == null) {
            file = new File(bb.getDataFolder(), "bounceBlock.yml");
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        bb.getLogger().log(Level.INFO, "bounceBlock.yml has been reloaded!");
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
            bb.getLogger().log(Level.SEVERE, "Could not save bounceBlock.yml!");
        }
    }


    @Override
    protected HashMap<Material, AbstractBlock> populateBlockMap() {
        ConfigurationSection section = customFile.getConfigurationSection("blocks");
        Set<String> keys = section.getKeys(false);

        if (!customFile.isConfigurationSection("blocks") || keys.isEmpty()) {
            bb.getLogger().log(Level.WARNING, "No blocks valid found in bounceBlocks.yml! ");
            return null;
        }

        if (keys.contains("EXAMPLE_BLOCK")) {
            bb.getLogger().log(Level.INFO, "EXAMPLE_BLOCK found in bounceBlocks.yml!");
        }

        HashMap<Material, AbstractBlock> validMats = new HashMap<>();
        keys.forEach(key -> {
            if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
                Material material = Material.getMaterial(key.toUpperCase());
                if (material == null || !bb.getBbManager().getValidBlocks().contains(material)) {
                    bb.getLogger().log(Level.WARNING,
                            "Material " + key + " bounceBlocs.yml is invalid! " +
                                    "Ignoring " + key + "!");
                } else {
                    String world = section.getString(key + ".world");
                    boolean include = section.getBoolean(key + ".include-world");
                    int height = section.getInt(key + ".height");
                    boolean normalize = section.getBoolean(key + ".normalize");

                    AbstractBlock block = new BounceBlock.BounceBuilder(material)
                            .withWorld(world).withIncludeWorld(include)
                            .withHeight(height).withNormalize(normalize).build();

                    validMats.put(material, block);
                }
            }
        });

        if (validMats.size() == 0) {
            bb.getLogger().log(Level.WARNING, "No valid entries in bounce configuration " +
                    "section found!");
            return null;
        }

        return validMats;
    }





}
