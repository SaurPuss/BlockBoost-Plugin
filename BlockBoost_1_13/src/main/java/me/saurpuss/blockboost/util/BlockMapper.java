package me.saurpuss.blockboost.util;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.blocks.LandmineBlock;
import me.saurpuss.blockboost.util.blocks.VelocityBlock;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

class BlockMapper {

    private BlockBoost bb;
    private FileConfiguration config;

    private final HashSet<Material> validBlocks;
    private final HashSet<VelocityBlock> velocityBlocks;
    private final HashSet<LandmineBlock> landmineBlocks;


    BlockMapper(BlockBoost plugin) {
        bb = plugin;
        config = bb.getConfig();
        validBlocks = blockMaterials();

        if (config.isConfigurationSection("block-boost.velocity"))
            velocityBlocks = setVelocityBlocks();
        else
            velocityBlocks = null;

        landmineBlocks = setLandmineBlocks();

    }

    boolean hasVelocityBlocks() {
        return velocityBlocks != null;
    }

    boolean hasLandmineBlocks() {
        return landmineBlocks != null;
    }

    HashSet<VelocityBlock> getVelocityBlocks() {
        return velocityBlocks;
    }

    HashSet<LandmineBlock> getLandmineBlocks() {
        return landmineBlocks;
    }

    private HashSet<VelocityBlock> setVelocityBlocks() {

        if (!config.isConfigurationSection("block-boost.velocity")) {
            bb.getLogger().log(Level.SEVERE, "Can't find default configuration section velocity! " +
                    "Please reset the default BlockBoost config file!");
            return null;
        }

        Set<String> section = config.getConfigurationSection("block-boost.velocity").getKeys(false);
        if (section.isEmpty()) {
            bb.getLogger().log(Level.WARNING, "No valid entries in velocity configuration " +
                    "section found!");
            return null;
        }

        if (section.contains("EXAMPLE_BLOCK")) {
            bb.getLogger().log(Level.WARNING, "EXAMPLE_BLOCK found in velocity configuration!");
        }

        HashSet<VelocityBlock> validMats = new HashSet<>();
        section.forEach(key -> {
            if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
                Material material = Material.getMaterial(key.toUpperCase());
                if (material == null || !validBlocks.contains(material)) {
                    bb.getLogger().log(Level.WARNING, "Material " + key + " in the config velocity " +
                            "section is invalid! Ignoring " + key + "!");
                } else {
                    VelocityBlock vBlock = new VelocityBlock();
                    vBlock.setMaterial(material);
                    vBlock.setMultiplier(config.getInt("block-boost.velocity." + key + ".multiplier"));
                    vBlock.setHeight(config.getInt("block-boost.velocity." + key + ".height"));
                    validMats.add(vBlock);
                }
            }
        });

        if (validMats.size() == 0) {
            bb.getLogger().log(Level.WARNING, "No valid entries in velocity configuration " +
                    "section found!");
            return null;
        }

        return validMats;
    }


    private HashSet<LandmineBlock> setLandmineBlocks() {
        if (!config.isConfigurationSection("block-boost.landmine")) {
            bb.getLogger().log(Level.SEVERE, "Can't find default configuration section landmine!");
            return null;
        }

        Set<String> section = config.getConfigurationSection("block-boost.landmine").getKeys(false);
        if (section.isEmpty()) {
            bb.getLogger().log(Level.WARNING, "No valid entries in landmine configuration " +
                    "section found!");
            return null;
        }

        if (section.contains("EXAMPLE_BLOCK")) {
            bb.getLogger().log(Level.WARNING, "EXAMPLE_BLOCK found in landmine configuration!");
        }

        HashSet<LandmineBlock> validMats = new HashSet<>();
        section.forEach(key -> {
            if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
                Material material = Material.getMaterial(key.toUpperCase());
                if (material == null || !validBlocks.contains(material)) {
                    bb.getLogger().log(Level.WARNING, "Material " + key + " in the config velocity " +
                            "section is invalid! Ignoring " + key + "!");
                } else {
                    LandmineBlock lBlock = new LandmineBlock();
                    lBlock.setMaterial(material);
                    lBlock.setDepth(config.getInt("block-boost.landmine." + key + ".depth"));
                    lBlock.setExplosion(config.getBoolean("block-boost.landmine." + key +
                            ".explosion"));

                    // Landmine has cover block
                    // TODO allow for multiple cover blocks
                    if (config.getBoolean("block-boost.landmine." + key + ".cover-block")) {
                        // Get cover block
                        Material block = null;
                        if (config.getString("block-boost.landmine." + key +
                                ".cover-block.block") != null) {
                            block = Material.getMaterial(config.getString("block-boost.landmine." +
                                    key + ".cover-block.block").toUpperCase());
                        }

                        // Check if cover block is valid for mapping
                        if (block == null) {
                            bb.getLogger().log(Level.SEVERE, "Invalid coverblock found in " +
                                    "landmine: " + key + " section! Ignoring " + key);
                        } else {
                            lBlock.setCoverBlock(block);
                            validMats.add(lBlock);
                        }
                    }
                    // Landmine doesn't need a cover block
                    else {
                        validMats.add(lBlock);
                    }
                }
            }
        });
        if (validMats.size() == 0) {
            bb.getLogger().log(Level.INFO, "No valid materials found in velocity config section.");
            return null;
        }

        return validMats;
    }

    private HashSet<Material> blockMaterials() {
        HashSet<Material> list = new HashSet<>();
        for (Material mat : Material.values()) {
            if (mat.isBlock()) {
                list.add(mat);
            }
        }

        return list;
    }
}
