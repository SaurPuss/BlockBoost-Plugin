package me.saurpuss.blockboost.util.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public abstract class CustomConfig {

    public abstract void setup();

    public abstract void reload();

    public abstract FileConfiguration get();

    public abstract void save();

    protected abstract HashMap<Material, AbstractBlock> populateBlockMap();

}
