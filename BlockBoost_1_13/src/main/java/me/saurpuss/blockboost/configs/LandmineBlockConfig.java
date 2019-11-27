package me.saurpuss.blockboost.configs;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.blocks.BuriedMineBlock;
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

public class LandmineBlockConfig extends AbstractConfig {

    private BlockBoost bb;

    private File file; // speedBlocks.yml location
    private FileConfiguration customFile;
    private final String FILE_NAME = "landmineBlocks.yml", SECTION_BURIED = "buried", SECTION_TRIP = "trip";

    private HashMap<Material, AbstractBlock> buriedMineBlockMap;
    private HashMap<Material, AbstractBlock> tripMineBlockMap;

    public LandmineBlockConfig(BlockBoost plugin) {
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
                bb.getLogger().log(Level.SEVERE, "Could not create " + FILE_NAME);
                buriedMineBlockMap = null;
                tripMineBlockMap = null;
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        saveCustomConfig();

        if (file.length() == 0 || customFile.getConfigurationSection("buried") == null ||
                customFile.getConfigurationSection("trip") == null) {
            bb.getLogger().log(Level.WARNING, "Invalid " + FILE_NAME + ", replacing with default " +
                    "configuration!");
            bb.saveResource(FILE_NAME, true);
        }

        // populate block maps
        if (hasValidKeys(BB.BURIED_MINE)) {
            bb.getLogger().log(Level.INFO, "Reading valid BuriedMineBlocks!");
            buriedMineBlockMap = populateBlockMap(BB.SPEED_ADDITION);
        } else
            buriedMineBlockMap = null;

        if (hasValidKeys(BB.TRIP_MINE)) {
            bb.getLogger().log(Level.INFO, "Reading valid TripMineBlocks!");
            tripMineBlockMap = populateBlockMap(BB.TRIP_MINE);
        } else
            tripMineBlockMap = null;
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
        ConfigurationSection section;

        switch (type) {
            case BURIED_MINE:
                section = customFile.getConfigurationSection(SECTION_BURIED);
                break;
            case TRIP_MINE:
                section = customFile.getConfigurationSection(SECTION_TRIP);
                break;
            default:
                bb.getLogger().log(Level.SEVERE, "Illegal attempt to check for valid keys in " + FILE_NAME);
                return false;
        }

        if (section == null) {
            bb.getLogger().log(Level.WARNING,
                    "No valid " + (type == BB.BURIED_MINE ? SECTION_BURIED : SECTION_TRIP) +
                            " path found in " + FILE_NAME + "! Ignoring LandmineBlocks!");
            return false;
        }

        Set<String> keys = section.getKeys(false);
        if (keys.isEmpty() || (!customFile.isConfigurationSection(SECTION_BURIED) &&
                !customFile.isConfigurationSection(SECTION_TRIP))) {
            bb.getLogger().log(Level.WARNING, "No blocks found in " + FILE_NAME + "! Ignoring " +
                    "LandmineBlocks!");
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
            case BURIED_MINE:
                section = customFile.getConfigurationSection(SECTION_BURIED);
                break;
            case TRIP_MINE:
                section = customFile.getConfigurationSection(SECTION_TRIP);
                break;
            default:
                bb.getLogger().log(Level.SEVERE, "Illegal attempt to populate block map with invalid " +
                        "keys in " + FILE_NAME + "!");
                return null;
        }


        assert section != null;
        Set<String> keys = section.getKeys(false);
        HashMap<Material, AbstractBlock> validMats = new HashMap<>();
        switch (type) {
            case BURIED_MINE:
                keys.forEach(key -> {
                    if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
                        Material material = Material.getMaterial(key.toUpperCase());
                        if (material == null || !material.isBlock()) {
                            bb.getLogger().log(Level.WARNING,
                                    "Material " + key + " in " + SECTION_BURIED + " " + FILE_NAME +
                                            " is invalid! Ignoring " + key + "!");
                        } else {
                            String world = section.getString(key + ".world");
                            boolean include = section.getBoolean(key + ".include-world");
                            int depth = section.getInt(key + ".depth");
                            boolean explosion = section.getBoolean(key + ".explosion");

                            AbstractBlock block = new BuriedMineBlock.Builder(material)
                                    .withWorld(world).withIncludeWorld(include).withDepth(depth)
                                    .withExplosion(explosion).build();

                            bb.getLogger().log(Level.INFO, "BuriedMineBlock added: " + block.toString());
                            validMats.put(material, block);
                        }
                    }
                });
                break;
            case TRIP_MINE:
//                keys.forEach(key -> {
//                    if (!key.equalsIgnoreCase("EXAMPLE_BLOCK")) {
//                        Material material = Material.getMaterial(key.toUpperCase());
//                        if (material == null || !material.isBlock()) {
//                            bb.getLogger().log(Level.WARNING,
//                                    "Material " + key + " in addition speedBlocks.yml is " +
//                                            "invalid! Ignoring " + key + "!");
//                        } else {
//                            String world = section.getString(key + ".world");
//                            boolean include = section.getBoolean(key + ".include-world");
//                            float addition = (float) section.getDouble(key + ".addition");
//                            int duration = section.getInt(key + ".duration");
//
//                            AbstractBlock block = new SpeedAdditionBlock.Builder(material)
//                                    .withWorld(world).withIncludeWorld(include)
//                                    .withAddition(addition).withDuration(duration)
//                                    .build();
//
//                            bb.getLogger().log(Level.INFO, "SpeedBlock added: " + block.toString());
//                            validMats.put(material, block);
//                        }
//                    }
//                });
                break;
            default:
                bb.getLogger().log(Level.SEVERE, "Illegal attempt to populate block map with invalid " +
                        (type == BB.BURIED_MINE ? SECTION_BURIED : SECTION_TRIP) + " keys in " + FILE_NAME + "!");
                return null;
        }

        if (validMats.size() == 0) {
            bb.getLogger().log(Level.WARNING,
                    "No valid landmine entries in configuration section " +
                            (type == BB.BURIED_MINE ? SECTION_BURIED : SECTION_TRIP) + " found!");
            return null;
        }

        return validMats;
    }

    @Override
    public HashMap<Material, AbstractBlock> getBlockMap(BB type) {
        switch (type) {
            case BURIED_MINE:
                return buriedMineBlockMap;
            case TRIP_MINE:
                return tripMineBlockMap;
            default:
                return null;
        }
    }
}
