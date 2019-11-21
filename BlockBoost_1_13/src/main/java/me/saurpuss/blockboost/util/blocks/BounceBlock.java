package me.saurpuss.blockboost.util.blocks;

import org.bukkit.Material;

public class BounceBlock {

    private Material material;
    private int height;
    private boolean normalize;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isNormalize() {
        return normalize;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }
}
