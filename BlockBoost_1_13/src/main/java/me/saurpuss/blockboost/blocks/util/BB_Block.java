package me.saurpuss.blockboost.blocks.util;

import org.bukkit.Material;

public interface BB_Block {

    Material getMaterial();

    String getWorld();
    void setWorld(String world);

    boolean isIncludeWorld();
    void setIncludeWorld(boolean includeWorld);
}
