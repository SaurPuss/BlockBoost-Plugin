package me.saurpuss.blockboost.util.util;

import org.bukkit.Material;

public abstract class AbstractBlock {

    public abstract Material getMaterial();
    protected abstract void setMaterial(Material material);

    public abstract String getWorld();
    public abstract void setWorld(String world);

    public abstract boolean isIncludeWorld();
    public abstract void setIncludeWorld(boolean includeWorld);
}
