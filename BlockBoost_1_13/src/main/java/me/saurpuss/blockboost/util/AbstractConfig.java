package me.saurpuss.blockboost.util;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public abstract class AbstractConfig {

    public abstract void setup();

    public abstract void loadCustomConfig();

    public abstract FileConfiguration getCustomConfig();

    public abstract void saveCustomConfig();

    protected abstract boolean hasValidKeys(BB type);

    protected abstract HashMap<Material, AbstractBlock> populateBlockMap(BB type);

    public abstract HashMap<Material, AbstractBlock> getBlockMap(BB type);
}
