package me.saurpuss.blockboost.util.blocks;

import org.bukkit.Material;

public class VelocityBlock {

    private Material material;
    private int multiplier;
    private int height;

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Material getMaterial() {
        return material;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getHeight() {
        return height;
    }
}
