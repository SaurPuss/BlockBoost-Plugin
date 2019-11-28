package me.saurpuss.blockboost.configs;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.SpeedAdditionBlock;
import me.saurpuss.blockboost.blocks.SpeedMultiplierBlock;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.AbstractConfig;
import me.saurpuss.blockboost.util.BB;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public class SpeedBlockConfig extends AbstractConfig {

    // TODO refactor with streams https://bukkit.gamepedia.com/Configuration_API_Reference#Arbitrary_Configurations

    private BlockBoost bb;

    private File file; // speedBlocks.yml location
    private FileConfiguration customFile;
    private final String FILE_NAME = "speedBlocks.yml", SECTION_MULTIPLIER = "multiplier",
            SECTION_ADDITION = "addition";

    private HashMap<Material, AbstractBlock> multiplierBlockMap;
    private HashMap<Material, AbstractBlock> additionBlockMap;

    public SpeedBlockConfig(BlockBoost plugin) {
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
                multiplierBlockMap = null;
                additionBlockMap = null;
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        bb.getLogger().log(Level.INFO, FILE_NAME + " exists in plugin directory!");

        saveCustomConfig();

        if (file.length() == 0 || customFile.getConfigurationSection(SECTION_MULTIPLIER) == null ||
                customFile.getConfigurationSection(SECTION_ADDITION) == null) {
            bb.saveResource(FILE_NAME, true);
            bb.getLogger().log(Level.WARNING, "Invalid " + FILE_NAME + ", replacing with default " +
                    "configuration!");
        }

        // populate block maps
        if (hasValidKeys(BB.SPEED_MULTIPLIER)) {
            bb.getLogger().log(Level.INFO, "Reading valid MultiplierSpeedBlocks!");
            multiplierBlockMap = populateBlockMap(BB.SPEED_MULTIPLIER);
        } else
            multiplierBlockMap = null;

        if (hasValidKeys(BB.SPEED_ADDITION)) {
            bb.getLogger().log(Level.INFO, "Reading valid AdditionSpeedBlocks!");
            additionBlockMap = populateBlockMap(BB.SPEED_ADDITION);
        } else
            additionBlockMap = null;
    }

    @Override
    public void loadCustomConfig() {
        if (customFile == null) {
            file = new File(bb.getDataFolder(), FILE_NAME);
        }
        customFile = YamlConfiguration.loadConfiguration(file);
        bb.getLogger().log(Level.INFO, FILE_NAME + " has been reloaded!");
    }

    @Override
    public FileConfiguration getCustomConfig() {
        return customFile;
    }


    @Override
    public void saveCustomConfig() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            bb.getLogger().log(Level.SEVERE, "Could not save " + FILE_NAME + "!");
        }
    }


    @Override
    protected boolean hasValidKeys(BB type) {
        ConfigurationSection section;

        switch (type) {
            case SPEED_MULTIPLIER:
                section = customFile.getConfigurationSection(SECTION_MULTIPLIER);
                break;
            case SPEED_ADDITION:
                section = customFile.getConfigurationSection(SECTION_ADDITION);
                break;
            default:
                bb.getLogger().log(Level.SEVERE, "Illegal attempt to check for valid keys " +
                        "in " + FILE_NAME + "!");
                return false;
        }

        if (section == null) {
            bb.getLogger().log(Level.WARNING,
                    "No valid " + (type == BB.SPEED_MULTIPLIER ? SECTION_MULTIPLIER : SECTION_ADDITION) +
                            " path found in " + FILE_NAME + "! Ignoring SpeedBlocks!");
            return false;
        }

        Set<String> keys = section.getKeys(false);
        if (keys.isEmpty() || (!customFile.isConfigurationSection(SECTION_MULTIPLIER) &&
                !customFile.isConfigurationSection(SECTION_ADDITION))) {
            bb.getLogger().log(Level.WARNING, "No blocks found in " + FILE_NAME + "! Ignoring SpeedBlocks!");
            return false;
        }

        if (keys.contains("EXAMPLE_BLOCK")) {
            bb.getLogger().log(Level.INFO, "EXAMPLE_BLOCK found in " + FILE_NAME + "!");
        }

        return true;
    }

    @Override
    protected HashMap<Material, AbstractBlock> populateBlockMap(BB type) {
        final ConfigurationSection section;
        switch (type) {
            case SPEED_MULTIPLIER:
                section = customFile.getConfigurationSection(SECTION_MULTIPLIER);
                break;
            case SPEED_ADDITION:
                section = customFile.getConfigurationSection(SECTION_ADDITION);
                break;
            default:
                bb.getLogger().log(Level.SEVERE, "Illegal attempt to populate block map with" +
                        " invalid keys in " + FILE_NAME + "!");
                return null;
        }


        assert section != null;
        Set<String> keys = section.getKeys(false);
        HashMap<Material, AbstractBlock> validMats = new HashMap<>();
        switch (type) {
            case SPEED_MULTIPLIER:
                keys.forEach(key -> {
                    if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
                        Material material = Material.getMaterial(key.toUpperCase());
                        if (material == null || !material.isBlock()) {
                            bb.getLogger().log(Level.WARNING,
                                    "Material " + key + " in " + SECTION_MULTIPLIER + " " + FILE_NAME + " " +
                                            "is " +
                                            "invalid! Ignoring " + key + "!");
                        } else {
                            String world = section.getString(key + ".world");
                            boolean include = section.getBoolean(key + ".include-world");
                            float defaultSpeed = (float) section.getDouble(key + ".default");
                            float speedMultiplier = (float) section.getDouble(key + ".multiplier");
                            float speedCap = (float) section.getDouble(key + ".cap");
                            int duration = section.getInt(key + ".duration");
                            int cooldown = section.getInt(key + ".cooldown");

                            AbstractBlock block = new SpeedMultiplierBlock.Builder(material)
                                    .withWorld(world).withIncludeWorld(include)
                                    .withDefaultSpeed(defaultSpeed)
                                    .withSpeedMultiplier(speedMultiplier).withCap(speedCap)
                                    .withDuration(duration).withCooldown(cooldown).build();

                            bb.getLogger().log(Level.INFO, "SpeedBlock added: " + block.toString());
                            validMats.put(material, block);
                        }
                    }
                });
                break;
            case SPEED_ADDITION:
                keys.forEach(key -> {
                    if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
                        Material material = Material.getMaterial(key.toUpperCase());
                        if (material == null || !material.isBlock()) {
                            bb.getLogger().log(Level.WARNING,
                                    "Material " + key + " in addition speedBlocks.yml is " +
                                            "invalid! Ignoring " + key + "!");
                        } else {
                            String world = section.getString(key + ".world");
                            boolean include = section.getBoolean(key + ".include-world");
                            float addition = (float) section.getDouble(key + ".addition");
                            int duration = section.getInt(key + ".duration");

                            AbstractBlock block = new SpeedAdditionBlock.Builder(material)
                                    .withWorld(world).withIncludeWorld(include)
                                    .withAddition(addition).withDuration(duration)
                                    .build();

                            bb.getLogger().log(Level.INFO, "SpeedBlock added: " + block.toString());
                            validMats.put(material, block);
                        }
                    }
                });
                break;
            default:
                bb.getLogger().log(Level.SEVERE, "Illegal attempt to populate block map with" +
                        " invalid keys in speedBlock.yml!");
                return null;
        }

        if (validMats.size() == 0) {
            bb.getLogger().log(Level.WARNING, "No valid entries in speed configuration " +
                    "section found!");
            return null;
        }

        return validMats;
    }

    @Override
    public HashMap<Material, AbstractBlock> getBlockMap(BB type) {
        switch (type) {
            case SPEED_MULTIPLIER:
                return multiplierBlockMap;
            case SPEED_ADDITION:
                return additionBlockMap;
            default:
                return null;
        }
    }
}
