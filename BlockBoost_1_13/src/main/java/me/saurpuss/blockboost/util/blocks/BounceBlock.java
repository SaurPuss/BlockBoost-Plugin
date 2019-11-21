package me.saurpuss.blockboost.util.blocks;

import org.bukkit.Material;

public class BounceBlock {

    private Material material;
    private int height;

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Material getMaterial() {
        return material;
    }

    public int getHeight() {
        return height;
    }

    public String toString() {
        return "material: " + material.toString() + " height: " + height;
    }
}
