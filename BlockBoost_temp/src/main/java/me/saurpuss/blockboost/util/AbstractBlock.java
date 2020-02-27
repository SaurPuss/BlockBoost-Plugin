package me.saurpuss.blockboost.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class AbstractBlock {

    public abstract Material getMaterial();

    protected abstract void setMaterial(Material material);

    public abstract String getWorld();

    public abstract void setWorld(String world);

    public abstract boolean isIncludeWorld();

    public abstract void setIncludeWorld(boolean includeWorld);

    public abstract String toColorString();

    public abstract String toString();

    public abstract void activate(final Player player);

}
