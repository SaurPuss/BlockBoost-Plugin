package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.*;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

class CustomBlockSetup {

    private BlockBoost bb;

    private YamlConfiguration bounceConfig, speedConfig, landmineConfig;

    private HashMap<Material, AbstractBlock> bounceBlockMap, speedAdditionBlockMap,
            speedMultiplierBlockMap, buriedMineBlockMap;

    CustomBlockSetup(BlockBoost plugin) {
        bb = plugin;

        if (!bb.getDataFolder().exists())
            bb.getDataFolder().mkdirs();

        for (BB type : BB.values()) {
            setup(type);
        }
    }


    private void setup(BB type) {
        File file = new File(bb.getDataFolder(), type.file());

        if (!file.exists()) {
            try {
                file.createNewFile();
                bb.saveResource(type.file(), true);
            } catch (IOException e) {
                bb.getLogger().log(Level.SEVERE, "Could not create " + type.file() + "!");
            }
        }

        loadCustomConfig(type);

        if (type.section() == null) {
            return;
        }

        if (hasValidKeys(type)) {
            populateBlockMap(type);
        }
    }


    private void loadCustomConfig(BB type) {
        File file = new File(bb.getDataFolder(), type.file());
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Check defaults in the jar
        Reader stream = new InputStreamReader(bb.getResource(type.file()));
        if (stream != null) {
            // TODO this doesn't actually copy defaults except for maybe the header
            YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(stream);
            config.setDefaults(customConfig);
            bb.getLogger().log(Level.INFO, "Read defaults to " + type.file() + "!");
        }

        switch (type) {
            case BOUNCE:
                bounceConfig = config;
                try {
                    bounceConfig.save(file);
                } catch (IOException e) {
                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
                }
                break;
            case SPEED:
                speedConfig = config;
                try {
                    speedConfig.save(file);
                } catch (IOException e) {
                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
                }
                break;
//            case LANDMINE:
//                landmineConfig = config;
//                try {
//                    landmineConfig.save(file);
//                } catch (IOException e) {
//                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
//                }
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
//            case LANDMINE:
//            case BURIED_MINE:
//            case TRIP_MINE:
//                if (landmineConfig == null) return;
//                try {
//                    landmineConfig.save(file);
//                } catch (IOException e) {
//                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
//                }
//                break;
//            case POTION:
        }
    }

    private YamlConfiguration getConfig(BB type) {
        switch (type) {
            case BOUNCE:
                return bounceConfig;
//            case LANDMINE:
//            case BURIED_MINE:
//            case TRIP_MINE:
//                return landmineConfig;
            case SPEED:
            case SPEED_ADDITION:
            case SPEED_MULTIPLIER:
                return speedConfig;
            default:
                return null;
        }
    }

    HashMap<Material, AbstractBlock> getBlockMap(BB type) {
        switch (type) {
            case BOUNCE:
                return bounceBlockMap;
            case SPEED_ADDITION:
                return speedAdditionBlockMap;
            case SPEED_MULTIPLIER:
                return speedMultiplierBlockMap;
//            case BURIED_MINE:
//                return buriedMineBlockMap;
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


        YamlConfiguration config = getConfig(type);

        if (!config.isSet(type.section())) {
            bb.getLogger().log(Level.WARNING, "No valid " + type.section() + " path found in " +
                    type.file() + "! Replacing file with default file!");
            bb.saveResource(type.file(), true);
        }

        if (!config.isConfigurationSection(type.section())) {
            bb.getLogger().log(Level.WARNING, type.section() + " not found in " + type.file() +
                    "!");
            return false;
        }

        Set<String> keys = config.getKeys(false);
        if (keys.size() == 0) {
            bb.getLogger().log(Level.INFO, "No blocks found in " + type.section() + "!");
            return false;
        }

        return true;
    }

    private void populateBlockMap(BB type) {
        assert getConfig(type) != null;
        ConfigurationSection section = getConfig(type).getConfigurationSection(type.section());
        if (section == null) return;

        Set<String> keys = section.getKeys(false);
        HashMap<Material, AbstractBlock> validMats = new HashMap<>();

        keys.forEach(key -> {
            Material material = Material.getMaterial(key.toUpperCase());
            if (material == null || !material.isBlock()) {
                bb.getLogger().log(Level.WARNING, "Material " + key + " in " + type.file() +
                        ":" + type.section() + " is invalid! Ignoring " + key + "!");
            } else {
                AbstractBlock block = null;
                String world = section.getString( key + ".world");
                boolean include = section.getBoolean(key + ".include-world");
                int duration = section.getInt(key + ".duration");;
                switch (type) {
                    case BOUNCE:
                        int height = section.getInt(key + ".height");
                        boolean normalize = section.getBoolean(key + ".normalize");

                        block = new BounceBlock.Builder(material).withWorld(world).withIncludeWorld(include)
                                .withHeight(height).withNormalize(normalize).build(); break;
                    case SPEED_ADDITION:
                        float addition = (float) section.getDouble(key + ".addition");
                        block = new SpeedAdditionBlock.Builder(material).withWorld(world)
                                .withIncludeWorld(include).withAddition(addition).withDuration(duration)
                                .build(); break;
                    case SPEED_MULTIPLIER:
                        float defaultSpeed = (float) section.getDouble(key + ".default");
                        float speedMultiplier = (float) section.getDouble(key + ".multiplier");
                        float speedCap = (float) section.getDouble(key + ".cap");
                        int cooldown = section.getInt(key + ".cooldown");

                        block = new SpeedMultiplierBlock.Builder(material).withWorld(world)
                                .withIncludeWorld(include).withDefaultSpeed(defaultSpeed)
                                .withSpeedMultiplier(speedMultiplier).withCap(speedCap)
                                .withDuration(duration).withCooldown(cooldown).build(); break;
//                    case BURIED_MINE:
//                        int depth = section.getInt(key + ".depth");
//                        boolean explosion = section.getBoolean(key + ".explosion");
//
//                        block = new BuriedMineBlock.Builder(material).withWorld(world)
//                                .withIncludeWorld(include).withDepth(depth).withCombustion(explosion)
//                                .build(); break;
                }

                if (block != null) {
                    validMats.put(material, block);
                    bb.getLogger().log(Level.INFO, "Block added - " + block.toString());
                }
            }
        });

        if (validMats.isEmpty()) {
            bb.getLogger().log(Level.WARNING, "No valid entries in " + type.section() + " found!");
        } else {
            switch (type) {
                case BOUNCE: bounceBlockMap = validMats; break;
                case SPEED_ADDITION: speedAdditionBlockMap = validMats; break;
                case SPEED_MULTIPLIER: speedMultiplierBlockMap = validMats; break;
//                case BURIED_MINE: buriedMineBlockMap = validMats; break;
            }
        }
    }
}
