package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.BounceBlock;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

class CustomBlockSetup {

    private BlockBoost bb;

    private YamlConfiguration bounceConfig, speedConfig, landmineConfig;

    private static HashMap<Material, AbstractBlock> bounceBlockMap, buriedMineBlockMap, speedAdditionBlockMap,
            speedMultiplierBlockMap;

    CustomBlockSetup(BlockBoost plugin) {
        bb = plugin;

        if (!bb.getDataFolder().exists())
            bb.getDataFolder().mkdirs();

        // Set up available BB's
        Arrays.asList(BB.values()).forEach(this::setup);
    }


    private void setup(BB type) {
        File file = new File(bb.getDataFolder(), type.file());

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                bb.getLogger().log(Level.SEVERE, "Could not create " + type.file() + "!");
            }
        }

        loadCustomConfig(type);
        saveCustomConfig(type);

        if (type.section() == null)
            return;

        if (hasValidKeys(type))
            populateBlockMap(type);
    }


    private void loadCustomConfig(BB type) {
        File file = new File(bb.getDataFolder(), type.file());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Check defaults in the jar
        Reader stream = new InputStreamReader(bb.getResource(type.file()));
        if (stream != null) {
            YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(stream);
            config.setDefaults(customConfig);
            bb.getLogger().log(Level.INFO, "Copied defaults to " + type.file() + "!");
        }

        switch (type) {
            case BOUNCE:
                bounceConfig = config;
                break;
            case SPEED:
                speedConfig = config;
                break;
            case LANDMINE:
                landmineConfig = config;
            default:
                return;
        }

        bb.getLogger().log(Level.INFO, type.file() + " has been reloaded!");
    }


    private void saveCustomConfig(BB type) {
        File file = new File(bb.getDataFolder(), type.file());

        switch (type) {
            case BOUNCE:
                if (bounceConfig == null) return;
                try {
                    bounceConfig.save(file);
                } catch (IOException e) {
                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
                }
                break;
            case SPEED:
            case SPEED_ADDITION:
            case SPEED_MULTIPLIER:
                if (speedConfig == null) return;
                try {
                    speedConfig.save(file);
                } catch (IOException e) {
                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
                }
                break;
            case LANDMINE:
            case BURIED_MINE:
            case TRIP_MINE:
                if (landmineConfig == null) return;
                try {
                    landmineConfig.save(file);
                } catch (IOException e) {
                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
                }
                break;
            case POTION:
        }
    }

    private YamlConfiguration getConfig(BB type) {
        switch (type) {
            case BOUNCE:
                return bounceConfig;
            case LANDMINE:
            case BURIED_MINE:
            case TRIP_MINE:
                return landmineConfig;
            case SPEED:
            case SPEED_ADDITION:
            case SPEED_MULTIPLIER:
                return speedConfig;
            default:
                return null;
        }
    }


    private boolean hasValidKeys(BB type) {
        if (type.section() == null) {
            bb.getLogger().log(Level.SEVERE, "Illegal attempt to check for valid boost blocks in " +
                    "custom configs!");
            return false;
        }

        ConfigurationSection section = getConfig(type).getConfigurationSection(type.section());
        if (section == null) {
            bb.getLogger().log(Level.WARNING, "No valid " + type.section() + " path found in " +
                    type.file() + "!");
            return false;
        }

        Set<String> keys = section.getKeys(false);
        if (!getConfig(type).isConfigurationSection(type.section()) || keys.isEmpty()) {
            bb.getLogger().log(Level.WARNING, "No blocks found in " + type.file() + "!");
            return false;
        }

        if (keys.contains("EXAMPLE_BLOCK")) {
            bb.getLogger().log(Level.INFO, "EXAMPLE_BLOCK found in " + type.file() + "!");
        }

        return true;
    }

    HashMap<Material, AbstractBlock> getBlockMap(BB type) {
        switch (type) {
            case BOUNCE:
                return bounceBlockMap;
            case SPEED_ADDITION:
                return speedAdditionBlockMap;
            case SPEED_MULTIPLIER:
                return speedMultiplierBlockMap;
            case BURIED_MINE:
                return buriedMineBlockMap;
            default:
                return null;
        }
    }

    private void populateBlockMap(BB type) {
        ConfigurationSection section = getConfig(type).getConfigurationSection(type.section());
        Set<String> keys = section.getKeys(false);
        HashMap<Material, AbstractBlock> validMats = new HashMap<>();

        keys.forEach(key -> {
            Material material = Material.getMaterial(key.toUpperCase());
            if (material == null || !material.isBlock()) {
                bb.getLogger().log(Level.WARNING, "Material " + key + " in " + type.file() +
                        " is invalid! Ignoring " + key + "!");
            } else {
                switch (type) {
                    case BOUNCE:
                        String world = section.getString(key + ".world");
                        boolean include = section.getBoolean(key + ".include-world");
                        int height = section.getInt(key + ".height");
                        boolean normalize = section.getBoolean(key + ".normalize");

                        AbstractBlock block = new BounceBlock.Builder(material)
                                .withWorld(world).withIncludeWorld(include)
                                .withHeight(height).withNormalize(normalize).build();

                        bb.getLogger().log(Level.INFO, "BounceBlock added: " + block.toString());
                        validMats.put(material, block);
                        break;
                    case SPEED_ADDITION:





                    default:
                        return;
                }
            }
        });

        if (validMats.size() == 0) {
            bb.getLogger().log(Level.WARNING, "No valid entries in " + type.section() + " found!");
            return;
        }

        switch (type) {
            case BOUNCE:
                bounceBlockMap = validMats;
                break;
            case SPEED_ADDITION:
                speedAdditionBlockMap = validMats;
                break;
            case SPEED_MULTIPLIER:
                speedMultiplierBlockMap = validMats;
                break;
        }
    }
}
