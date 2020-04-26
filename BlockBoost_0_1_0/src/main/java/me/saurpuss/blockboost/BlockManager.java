package me.saurpuss.blockboost;

import me.saurpuss.blockboost.blocks.single.*;
import me.saurpuss.blockboost.listeners.CommonBlockListener;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.logging.Level;

import static java.util.stream.Collectors.toList;

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
            if (!temp.isEmpty()) {
                blocks.addAll(temp.values());
                // TODO send their strings to the list blocks command class?
            }
        }

        if (!blocks.isEmpty())
            new CommonBlockListener(bb, blocks);
        else
            bb.getLogger().log(Level.INFO, "No valid Boost Blocks found!");
    }

    private HashMap<Material, AbstractBlock> fillBlockMap(BB type) {
        ConfigurationSection section = config.getConfigurationSection(type.section());
        if (section == null) return new HashMap<>();

        Set<String> keys = section.getKeys(false);
        HashMap<Material, AbstractBlock> map = new HashMap<>();
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
//                    case COMMAND:
//                        boolean consoleSender = section.getBoolean(key + ".console-sender");
//                        String command = section.getString(key + ".command");
//                        String permission = section.getString(key + ".permission");
//
//                        block = new CommandBlock.Builder(material).withWorld(world)
//                                .withIncludeWorld(include).withConsoleSender(consoleSender)
//                                .withCommand(command).withPermission(permission).build();
//                        break;
//                    case EXPLOSION:
//                        float power = (float) section.getDouble(key + ".power");
//                        boolean setFire = section.getBoolean(key + ".set-fire");
//                        boolean breakBlocks = section.getBoolean(key + ".break-blocks");
//                        double playerDamage = section.getDouble(key + ".player-damage");
//                        boolean playerKill = section.getBoolean(key + ".player-kill");
//
//                        block = new ExplosionBlock.Builder(material).withWorld(world)
//                                .withIncludeWorld(include).withPower(power).withSetFire(setFire)
//                                .withBreakBlocks(breakBlocks).withPlayerDamage(playerDamage)
//                                .withPlayerKill(playerKill).build();
//                        break;
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
//                    case SPEED:
//                        String subType = section.getString(key + ".type");
//                        float amount = (float) section.getDouble(key + ".amount");
//                        float cap = (float)  section.getDouble(key + ".cap");
//                        long cooldown = section.getLong(key + ".cooldown");
//
//                        block = new SpeedBlock.Builder(material).withWorld(world)
//                                .withIncludeWorld(include).withType(Objects.requireNonNull(subType,
//                                        "Speed Block type cannot be null!"))
//                                .withAmount(amount).withCap(cap).withDuration(duration)
//                                .withCooldown(cooldown).build();
//                        break;
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

    public List<AbstractBlock> getBlockList(BB type) {
        List<AbstractBlock> list = new ArrayList<>();

        switch (type) {
            case BOUNCE:
                list = blocks.stream().filter(BounceBlock.class::isInstance)
                        .map(BounceBlock.class::cast).collect(toList());
                break;
//            case COMMAND:
//                list = blocks.stream().filter(CommandBlock.class::isInstance)
//                        .map(CommandBlock.class::cast).collect(toList());
//                break;
//            case EXPLOSION:
//                list = blocks.stream().filter(ExplosionBlock.class::isInstance)
//                        .map(ExplosionBlock.class::cast).collect(toList());
//                break;
            case POTION:
                list = blocks.stream().filter(PotionEffectBlock.class::isInstance)
                        .map(PotionEffectBlock.class::cast).collect(toList());
                break;
//            case SPEED:
//                list = blocks.stream().filter(SpeedBlock.class::isInstance)
//                        .map(SpeedBlock.class::cast).collect(toList());
        }

        return list;
    }
}
