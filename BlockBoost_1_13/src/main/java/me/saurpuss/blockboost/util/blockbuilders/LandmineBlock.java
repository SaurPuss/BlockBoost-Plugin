package me.saurpuss.blockboost.util.blockbuilders;

import org.bukkit.Material;

public class LandmineBlock {

    private Material material;
    private int depth;
    private boolean explosion;
    private Material coverBlock;

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setExplosion(boolean explosion) {
        this.explosion = explosion;
    }

    public void setCoverBlock(Material coverBlock) {
        this.coverBlock = coverBlock;
    }

    public Material getMaterial() {
        return material;
    }

    public int getDepth() {
        return depth;
    }

    public Material getCoverBlock() {
        return coverBlock;
    }

    public boolean isExplosion() {
        return explosion;
    }

    public boolean hasCoverBlock(){
        return coverBlock != null;
    }
}
