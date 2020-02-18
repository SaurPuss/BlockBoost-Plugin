package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.*;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BB;
import me.saurpuss.blockboost.util.BBSubType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Load configuration files for valid block creation.
 */
class CustomBlockSetup {

    private final BlockBoost bb;
    private YamlConfiguration bounceConfig, speedConfig, potionConfig;
    private HashMap<Material, AbstractBlock> bounceBlockMap, speedBlockMap, potionEffectBlockMap;

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

        if (hasValidKeys(type))
            populateBlockMap(type);
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
            case POTION:
                potionConfig = config;
                try {
                    potionConfig.save(file);
                } catch (IOException e) {
                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
                }
                break;
            default:
                bb.getLogger().log(Level.WARNING, "Unknown BB of type " + type + " found in " +
                        "CustomBlockSetup#loadCustomConfig()! Please contact the developer!");
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
                if (speedConfig == null) return;
                try {
                    speedConfig.save(file);
                } catch (IOException e) {
                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
                }
                break;
            case POTION:
                if (potionConfig == null) return;
                try {
                    potionConfig.save(file);
                } catch (IOException e) {
                    bb.getLogger().log(Level.SEVERE, "Could not save " + type.file() + "!");
                }
                break;
            default:
                bb.getLogger().log(Level.WARNING, "Unknown BB of type " + type + " found in " +
                        "CustomBlockSetup#saveCustomConfig()! Please contact the developer!");
        }
    }

    private YamlConfiguration getConfig(BB type) {
        switch (type) {
            case BOUNCE:
                return bounceConfig;
            case SPEED:
                return speedConfig;
            case POTION:
                return potionConfig;
            default:
                bb.getLogger().log(Level.WARNING, "Unknown BB of type " + type + "found in " +
                        "CustomBlockSetup#getCustomConfig()!");
                return null;
        }
    }

    HashMap<Material, AbstractBlock> getBlockMap(BB type) {
        switch (type) {
            case BOUNCE:
                return bounceBlockMap;
            case SPEED:
                return speedBlockMap;
            case POTION:
                return potionEffectBlockMap;
            default:
                bb.getLogger().log(Level.WARNING, "Unregistered or unknown BB of type " + type +
                        " found in CustomBlockSetup#getBlockMap()!");
                return null;
        }
    }

    private boolean hasValidKeys(BB type) {
        if (type.section() == null) {
            bb.getLogger().log(Level.SEVERE,
                    "Illegal attempt to check for valid " + type + " blocks in custom configs!");
            return false;
        }

        YamlConfiguration config = getConfig(type);

        if (config == null || !config.isSet(type.section())) {
            bb.getLogger().log(Level.WARNING, "No valid " + type.section() + " path found in " +
                    type.file() + "! Replacing file with default file!");
            bb.saveResource(type.file(), true);
            config = getConfig(type);
        }

        if (config == null || !config.isConfigurationSection(type.section())) {
            bb.getLogger().log(Level.WARNING, type.section() + " not found in " + type.file() + "!");
            return false;
        }

        Set<String> keys = config.getKeys(false);
        if (keys.size() == 0) {
            bb.getLogger().log(Level.INFO, "No blocks found in " + type.section() + "!");
            return false;
        }

        return true;
    }

    /**
     * Retrieve block map corresponding to sections in the config files.
     * @param type BB enum
     */
    private void populateBlockMap(BB type) {
        YamlConfiguration config = getConfig(type);
        if (config == null)
            return;

        ConfigurationSection section = config.getConfigurationSection(type.section());
        if (section == null)
            return;

        Set<String> keys = section.getKeys(false);
        HashMap<Material, AbstractBlock> validMats = new HashMap<>();

        keys.forEach(key -> {
            Material material = Material.getMaterial(key.toUpperCase());
            if (material == null || !material.isBlock()) {
                bb.getLogger().log(Level.WARNING, "Material " + key + " in " + type.file() +
                        ":" + type.section() + " is invalid! Ignoring " + key + "!");
            } else {
                // Set the common variables
                boolean consoleMessage = false;
                AbstractBlock block = null;
                String world = section.getString( key + ".world");
                boolean include = section.getBoolean(key + ".include-world");
                long duration = section.getLong(key + ".duration");

                // Try to create a corresponding block type
                switch (type) {
                    case BOUNCE:
                        consoleMessage = bounceConfig.getBoolean("console-message");
                        int height = section.getInt(key + ".height");
                        boolean normalize = section.getBoolean(key + ".normalize");

                        block = new BounceBlock.Builder(material).withWorld(world)
                                .withIncludeWorld(include).withHeight(height)
                                .withNormalize(normalize).build();
                        break;
                    case SPEED:
                        consoleMessage = speedConfig.getBoolean("console-message");
                        String typeString = section.getString(key + ".type");
                        float amount = (float) section.getDouble(key + ".amount");
                        float cap = (float)  section.getDouble(key + ".cap");
                        long cooldown = section.getLong(key + ".cooldown");
                        if (typeString != null) {
                            BBSubType subType = BBSubType.getByName(typeString);
                            if (subType != null) {
                                block = new SpeedBlock.Builder(material).withWorld(world)
                                        .withIncludeWorld(include).withType(subType).withAmount(amount)
                                        .withCap(cap).withDuration(duration).withCooldown(cooldown)
                                        .build();
                            } else {
                                bb.getLogger().log(Level.WARNING, "SpeedBlockType " + typeString +
                                        " in " + type.file() + ":" + type.section() + " is invalid! " +
                                        "Ignoring " + key + "!");
                            }
                        } else {
                            bb.getLogger().log(Level.WARNING,
                                    "Missing SpeedBlockType in " + type.file() + ":" + type.section() +
                                            "! Ignoring " + key + "! ");
                        }
                        break;
                    case POTION:
                        consoleMessage = potionConfig.getBoolean("console-message");
                        String effect = section.getString(key + ".effect");
                        int amplifier = section.getInt(key + ".amplifier");
                        if (effect != null) {
                            PotionEffectType effectType = PotionEffectType.getByName(effect);
                            if (effectType != null) {
                                block = new PotionEffectBlock.Builder(material).withWorld(world)
                                        .withIncludeWorld(include).withEffectType(effectType)
                                        .withDuration((int)duration).withAmplifier(amplifier).build();
                            } else {
                                bb.getLogger().log(Level.WARNING, "PotionEffectType " + effect +
                                        " in " + type.file() + ":" + type.section() + " is invalid! " +
                                        "Ignoring " + key + "!");
                            }
                        } else {
                            bb.getLogger().log(Level.WARNING,
                                    "Missing PotionEffectType in " + type.file() + ":" + type.section() +
                                            "! Ignoring " + key + "!");
                        }
                        break;
                }

                if (block != null) {
                    validMats.put(material, block);
                    if (consoleMessage)
                        bb.getLogger().log(Level.INFO, type + " Block added - " + block.toString());
                }
            }
        });

        if (validMats.isEmpty()) {
            bb.getLogger().log(Level.WARNING, "No valid entries in " + type.section() + " found!");
        } else {
            switch (type) {
                case BOUNCE: bounceBlockMap = validMats; break;
                case SPEED: speedBlockMap = validMats; break;
                case POTION: potionEffectBlockMap = validMats; break;
            }
        }
    }
}
