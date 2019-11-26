package me.saurpuss.blockboost.managers;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.configs.BounceBlockConfig;
import me.saurpuss.blockboost.util.util.AbstractBlockFactory;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;

class BlockMapper {

    private BlockBoost bb;
    private final HashSet<Material> validBlocks;

//    private final HashSet<BounceBlock> bounceBlocks;
//    private final HashSet<SpeedMultiplierBlock> speedMultiplierBlocks;
//    private final HashSet<LandmineBlock> landmineBlocks;


    BlockMapper(BlockBoost plugin) {
        bb = plugin;
//        config = bb.getConfig();
        validBlocks = blockMaterials();

        bb.getConfigManager().getBounceBlockConfig();


        new BounceBlockConfig(bb);

//        bounceBlocks = setBounceBlocks();
//        speedMultiplierBlocks = setSpeedBlocks();
//
//        landmineBlocks = setLandmineBlocks();

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

    public HashSet<Material> getValidBlocks() {
        return validBlocks;
    }

//    boolean hasVelocityBlocks() {
//        return bounceBlocks != null;
//    }
//
//    boolean hasSpeedBlocks() {
//        return speedMultiplierBlocks != null;
//    }
//
//    boolean hasLandmineBlocks() {
//        return landmineBlocks != null;
//    }
//
//    HashSet<BounceBlock> getBounceBlocks() {
//        return bounceBlocks;
//    }
//
//    HashSet<SpeedMultiplierBlock> getSpeedMultiplierBlocks() {
//        return speedMultiplierBlocks;
//    }
//
//    HashSet<LandmineBlock> getLandmineBlocks() {
//        return landmineBlocks;
//    }

    // TODO combine these into a single reusable method
//    private HashSet<BounceBlock> setBounceBlocks() {
//        if (!config.isConfigurationSection("block-boost.bounce")) {
//            bb.getLogger().log(Level.SEVERE, "Can't find default configuration section bounce! " +
//                    "Please reset the default BlockBoost config file!");
//            return null;
//        }
//
//        Set<String> section = config.getConfigurationSection("block-boost.bounce").getKeys(false);
//        if (section.isEmpty()) {
//            bb.getLogger().log(Level.WARNING, "No valid entries in bounce configuration " +
//                    "section found!");
//            return null;
//        }
//
//        if (section.contains("EXAMPLE_BLOCK")) {
//            bb.getLogger().log(Level.WARNING, "EXAMPLE_BLOCK found in bounce configuration!");
//        }
//
//        HashMap<Material, BounceBlock> validMats = new HashMap<>();
//        section.forEach(key -> {
//            if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
//                Material material = Material.getMaterial(key.toUpperCase());
//                if (material == null || !validBlocks.contains(material)) {
//                    bb.getLogger().log(Level.WARNING, "Material " + key + " in the config bounce " +
//                            "section is invalid! Ignoring " + key + "!");
//                } else {
//                    BounceBlock vBlock = new BounceBlock();
//                    vBlock.setMaterial(material);
//                    vBlock.setHeight(config.getInt("block-boost.bounce." + key + ".height"));
//                    vBlock.setNormalize(config.getBoolean("block-boost.bounce." + key +
//                            ".normalize"));
//                    validMats.put(material, vBlock);
//                }
//            }
//        });
//
//        if (validMats.size() == 0) {
//            bb.getLogger().log(Level.WARNING, "No valid entries in bounce configuration " +
//                    "section found!");
//            return null;
//        }
//
//
//        return new HashSet<>(validMats.values());
//    }
//
//    private HashSet<SpeedMultiplierBlock> setSpeedBlocks() {
//        if (!config.isConfigurationSection("block-boost.speed")) {
//            bb.getLogger().log(Level.SEVERE, "Can't find default configuration section speed! " +
//                    "Please reset the default BlockBoost config file!");
//            return null;
//        }
//
//        Set<String> section = config.getConfigurationSection("block-boost.speed").getKeys(false);
//        if (section.isEmpty()) {
//            bb.getLogger().log(Level.WARNING, "No valid entries in speed configuration " +
//                    "section found!");
//            return null;
//        }
//
//        if (section.contains("EXAMPLE_BLOCK")) {
//            bb.getLogger().log(Level.WARNING, "EXAMPLE_BLOCK found in speed configuration!");
//        }
//
//        HashMap<Material, SpeedMultiplierBlock> validMats = new HashMap<>();
//        section.forEach(key -> {
//            if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
//                Material material = Material.getMaterial(key.toUpperCase());
//                if (material == null || !validBlocks.contains(material)) {
//                    bb.getLogger().log(Level.WARNING, "Material " + key + " in the config speed " +
//                            "section is invalid! Ignoring " + key + "!");
//                } else {
//                    SpeedMultiplierBlock sBlock = new SpeedMultiplierBlock();
//                    sBlock.setMaterial(material);
//                    sBlock.setDefaultSpeed((float)config.getDouble("block-boost.speed." + key +
//                            ".default"));
//                    sBlock.setSpeedMultiplier((float)config.getDouble("block-boost.speed." + key +
//                            ".multiplier"));
//                    sBlock.setSpeedCap((float)config.getDouble("block-boost.speed." + key +
//                            ".cap"));
//                    sBlock.setDuration(config.getLong("block-boost.speed." + key + ".duration"));
//                    sBlock.setCooldown(config.getLong("block-boost.speed." + key + ".cooldown"));
//                    validMats.put(material, sBlock);
//                }
//            }
//        });
//
//        if (validMats.size() == 0) {
//            bb.getLogger().log(Level.WARNING, "No valid entries in speed configuration " +
//                    "section found!");
//            return null;
//        }
//
//        return new HashSet<>(validMats.values());
//    }
//
//
//    private HashSet<LandmineBlock> setLandmineBlocks() {
//        if (!config.isConfigurationSection("block-boost.landmine")) {
//            bb.getLogger().log(Level.SEVERE, "Can't find default configuration section landmine!");
//            return null;
//        }
//
//        Set<String> section = config.getConfigurationSection("block-boost.landmine").getKeys(false);
//        if (section.isEmpty()) {
//            bb.getLogger().log(Level.WARNING, "No valid entries in landmine configuration " +
//                    "section found!");
//            return null;
//        }
//
//        if (section.contains("EXAMPLE_BLOCK")) {
//            bb.getLogger().log(Level.WARNING, "EXAMPLE_BLOCK found in landmine configuration!");
//        }
//
//        HashMap<Material, LandmineBlock> validMats = new HashMap<>();
//        section.forEach(key -> {
//            if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
//                Material material = Material.getMaterial(key.toUpperCase());
//                if (material == null || !validBlocks.contains(material)) {
//                    bb.getLogger().log(Level.WARNING, "Material " + key + " in the config velocity " +
//                            "section is invalid! Ignoring " + key + "!");
//                } else {
//
//                    // TODO FULL REWORK
//                    LandmineBlock lBlock = new LandmineBlock();
//                    lBlock.setMaterial(material);
//                    lBlock.setDepth(config.getInt("block-boost.landmine." + key + ".depth"));
//                    lBlock.setExplosion(config.getBoolean("block-boost.landmine." + key +
//                            ".explosion"));
//
//                    // Landmine has cover block
//                    // TODO allow for multiple cover blocks
//                    if (config.getBoolean("block-boost.landmine." + key + ".cover-block")) {
//                        // Get cover block
//                        Material block = null;
//                        if (config.getString("block-boost.landmine." + key +
//                                ".cover-block.block") != null) {
//                            block = Material.getMaterial(config.getString("block-boost.landmine." +
//                                    key + ".cover-block.block").toUpperCase());
//                        }
//
//                        // Check if cover block is valid for mapping
//                        if (block == null) {
//                            bb.getLogger().log(Level.SEVERE, "Invalid coverblock found in " +
//                                    "landmine: " + key + " section! Ignoring " + key);
//                        } else {
//                            lBlock.setCoverBlock(block);
//                            validMats.put(material, lBlock);
//                        }
//                    }
//                    // Landmine doesn't need a cover block
//                    else {
//                        validMats.put(material, lBlock);
//                    }
//                }
//            }
//        });
//        if (validMats.size() == 0) {
//            bb.getLogger().log(Level.INFO, "No valid materials found in velocity config section.");
//            return null;
//        }
//
//        return new HashSet<>(validMats.values());
//    }
//

}
