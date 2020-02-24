package me.saurpuss.blockboost;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.BounceBlock;
import me.saurpuss.blockboost.blocks.CommandBlock;
import me.saurpuss.blockboost.blocks.PotionEffectBlock;
import me.saurpuss.blockboost.blocks.SpeedBlock;
import me.saurpuss.blockboost.listeners.CommonBlockListener;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BB;
import me.saurpuss.blockboost.util.BBSubType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

public class BlockManager {

    private final BlockBoost bb;
    private final FileConfiguration config;
    private Set<AbstractBlock> blocks;

    public BlockManager(BlockBoost plugin) {
        bb = plugin;
        config = bb.getConfig();

        blocks = new LinkedHashSet<>();
        for (BB bb : BB.values()) {
            HashMap<Material, AbstractBlock> temp = fillBlockMap(bb);
            if (!temp.isEmpty())
                blocks.addAll(temp.values());
        }

        if (!blocks.isEmpty())
            new CommonBlockListener(bb, blocks);
        else
            bb.getLogger().log(Level.INFO, "No valid Boost Blocks found!");
    }

    private HashMap<Material, AbstractBlock> fillBlockMap(BB type) {
        HashMap<Material, AbstractBlock> map = new HashMap<>();

        ConfigurationSection section = config.getConfigurationSection(type.section());
        if (section == null) return new HashMap<>();

        Set<String> keys = section.getKeys(false);
        keys.forEach(key -> {
            Material material = Material.getMaterial(key.toUpperCase());
            if (material == null || !material.isBlock()) {
                bb.getLogger().log(Level.WARNING, "Material " + key + " in " + type.section() +
                        " is invalid! Ignoring " + key + "!");
            } else {
                // Set the common variables
                boolean consoleMessage = config.getBoolean("console-messages." + type.section());
                AbstractBlock block = null;
                String world = section.getString( key + ".world");
                boolean include = section.getBoolean(key + ".include-world");
                long duration = section.getLong(key + ".duration");

                // Try to create a corresponding block type
                switch (type) {
                    case BOUNCE:
                        int height = section.getInt(key + ".height");
                        boolean normalize = section.getBoolean(key + ".normalize");

                        block = new BounceBlock.Builder(material).withWorld(world)
                                .withIncludeWorld(include).withHeight(height)
                                .withNormalize(normalize).build();
                        break;
                    case COMMAND:
                        boolean consoleSender = section.getBoolean(key + ".console-sender");
                        String command = section.getString(key + ".command");
                        String permission = section.getString(key + ".permission");

                        block = new CommandBlock.Builder(material).withWorld(world)
                                .withIncludeWorld(include).withConsoleSender(consoleSender)
                                .withCommand(command).withPermission(permission).build();
                        break;
                    case POTION:
                        String effect = section.getString(key + ".effect");
                        int amplifier = section.getInt(key + ".amplifier");

                        PotionEffectType effectType = PotionEffectType.getByName(effect);
                        if (effectType != null) {
                            block = new PotionEffectBlock.Builder(material).withWorld(world)
                                    .withIncludeWorld(include).withEffectType(effectType)
                                    .withDuration((int)duration).withAmplifier(amplifier).build();
                        } else {
                            bb.getLogger().log(Level.WARNING, "PotionEffectType " + effect +
                                    " in " + type.section() + " is invalid! Ignoring " + key + "!");
                        }
                        break;
                    case SPEED:
                        String typeString = section.getString(key + ".type");
                        float amount = (float) section.getDouble(key + ".amount");
                        float cap = (float)  section.getDouble(key + ".cap");
                        long cooldown = section.getLong(key + ".cooldown");

                        BBSubType subType = BBSubType.getByName(Objects.requireNonNull(typeString,
                                        "SPEED_ADDITION"));
                        if (subType != null) {
                            block = new SpeedBlock.Builder(material).withWorld(world)
                                    .withIncludeWorld(include).withType(subType).withAmount(amount)
                                    .withCap(cap).withDuration(duration).withCooldown(cooldown)
                                    .build();
                        } else {
                            bb.getLogger().log(Level.WARNING, "SpeedBlockType " + typeString +
                                    " in " + type.section() + " is invalid! Ignoring " + key + "!");
                        }
                        break;
                }

                if (block != null) {
                    map.put(material, block);
                    if (consoleMessage)
                        bb.getLogger().log(Level.INFO, type + " Block added - " + block.toString());
                }
            }
        });

        if (map.isEmpty())
            bb.getLogger().log(Level.WARNING, "No valid entries in " + type.section() + " found!");


        return map;
    }
}
