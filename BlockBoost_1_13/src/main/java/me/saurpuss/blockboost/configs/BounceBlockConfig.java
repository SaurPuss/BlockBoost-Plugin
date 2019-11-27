package me.saurpuss.blockboost.configs;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.BounceBlock;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractConfig;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public class BounceBlockConfig extends AbstractConfig {

    private BlockBoost bb;

    private File file; // bounceBlocks.yml location
    private FileConfiguration customFile;
    private final String FILE_NAME = "bounceBlocks.yml", SECTION = "bounce";

    private HashMap<Material, AbstractBlock> blockMap;

    public BounceBlockConfig(BlockBoost plugin) {
        bb = plugin;
    }

    @Override
    public void setup() {
        if (!bb.getDataFolder().exists())
            bb.getDataFolder().mkdirs();

        file = new File(bb.getDataFolder(), FILE_NAME);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                bb.getLogger().log(Level.SEVERE, "Could not create " + FILE_NAME + "!");
                blockMap = null;
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        bb.getLogger().log(Level.INFO, FILE_NAME + " exists in plugin directory!");

        saveCustomConfig();

        if (file.length() == 0 || customFile.getConfigurationSection("blocks") == null) {
            bb.saveResource(FILE_NAME, true);
            bb.getLogger().log(Level.WARNING, "Invalid "+ FILE_NAME + ", replacing with default " +
                    "configuration!");
        }

        // populate blockmap
        if (hasValidKeys(BB.BOUNCE)) {
            bb.getLogger().log(Level.INFO, "Reading valid BounceBlocks!");
            blockMap = populateBlockMap(BB.BOUNCE);
        } else
            blockMap = null;
    }

    @Override
    public void loadCustomConfig() {
        if (file == null)
            file = new File(bb.getDataFolder(), FILE_NAME);

        customFile = YamlConfiguration.loadConfiguration(file);

        // Check defaults in the jar
        Reader stream = new InputStreamReader(bb.getResource(FILE_NAME));
        if (stream != null) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(stream);
            customFile.setDefaults(config);
            bb.getLogger().log(Level.INFO, "Copied defaults to " + FILE_NAME + "!");
        }

        bb.getLogger().log(Level.INFO, FILE_NAME + " has been loaded!");
    }

    @Override
    public FileConfiguration getCustomConfig() {
        if (customFile == null)
            loadCustomConfig();

        return customFile;
    }


    @Override
    public void saveCustomConfig() {
        if (file == null || customFile == null)
            return;

        try {
            customFile.save(file);
        } catch (IOException e) {
            bb.getLogger().log(Level.SEVERE, "Could not save " + FILE_NAME + "!");
        }
    }


    @Override
    protected boolean hasValidKeys(BB type) {
        if (type != BB.BOUNCE) {
            bb.getLogger().log(Level.SEVERE, "Illegal attempt to check for valid keys in " + FILE_NAME);
            return false;
        }

        ConfigurationSection section = customFile.getConfigurationSection(SECTION);
        if (section == null) {
            bb.getLogger().log(Level.WARNING, "No valid " + SECTION + " path found in " + FILE_NAME +
                    "! Ignoring BounceBlocks!");
            return false;
        }

        Set<String> keys = section.getKeys(false);
        if (!customFile.isConfigurationSection("blocks") || keys.isEmpty()) {
            bb.getLogger().log(Level.WARNING, "No blocks found in " + FILE_NAME + "! Ignoring BounceBlocks!");
            return false;
        }

        if (keys.contains("EXAMPLE_BLOCK")) {
            bb.getLogger().log(Level.INFO, "EXAMPLE_BLOCK found in " + FILE_NAME + "!");
        }

        return true;
    }

    @Override
    protected HashMap<Material, AbstractBlock> populateBlockMap(BB type) {
        if (type != BB.BOUNCE)
            return null;

        ConfigurationSection section = customFile.getConfigurationSection(SECTION);
        Set<String> keys = section.getKeys(false);

        HashMap<Material, AbstractBlock> validMats = new HashMap<>();
        keys.forEach(key -> {
            Material material = Material.getMaterial(key.toUpperCase());
            if (material == null || !material.isBlock()) {
                bb.getLogger().log(Level.WARNING,
                        "Material " + key + " in " + FILE_NAME + " is invalid! " +
                                "Ignoring " + key + "!");
            } else {
                String world = section.getString(key + ".world");
                boolean include = section.getBoolean(key + ".include-world");
                int height = section.getInt(key + ".height");
                boolean normalize = section.getBoolean(key + ".normalize");

                AbstractBlock block = new BounceBlock.Builder(material)
                        .withWorld(world).withIncludeWorld(include)
                        .withHeight(height).withNormalize(normalize).build();

                bb.getLogger().log(Level.INFO, "BounceBlock added: " + block.toString());
                validMats.put(material, block);
            }
        });

        if (validMats.size() == 0) {
            bb.getLogger().log(Level.WARNING, "No valid entries in " + SECTION + " configuration " +
                    "section found!");
            return null;
        }

        return validMats;
    }

    @Override
    public HashMap<Material, AbstractBlock> getBlockMap(BB type) {
        if (type != BB.BOUNCE)
            return null;

        return blockMap;
    }
}
