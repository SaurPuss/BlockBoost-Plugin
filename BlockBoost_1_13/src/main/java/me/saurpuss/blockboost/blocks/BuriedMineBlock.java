package me.saurpuss.blockboost.blocks;

import me.saurpuss.blockboost.util.AbstractBlock;
import org.bukkit.Material;

public class BuriedMineBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private int depth;
        private boolean explosion;

        public Builder(Material material) {
            this.material = material;
        }

        public Builder withWorld(String world) {
            this.world = world;

            return this;
        }

        public Builder withIncludeWorld(boolean includeWorld) {
            this.includeWorld = includeWorld;

            return this;
        }

        public Builder withDepth(int depth) {
            depth = Math.abs(depth);
            this.depth = depth;

            return this;
        }

        public Builder withExplosion(boolean explosion) {
            this.explosion = explosion;

            return this;
        }

        public BuriedMineBlock build() {
            BuriedMineBlock block = new BuriedMineBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.depth = this.depth;
            block.explosion = this.explosion;

            return block;
        }
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private int depth;
    private boolean explosion;

    private BuriedMineBlock() {}

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(Material material) {
        this.material = material;
    }

    @Override
    public String getWorld() {
        return world;
    }

    @Override
    public void setWorld(String world) {
        this.world = world;
    }

    @Override
    public boolean isIncludeWorld() {
        return includeWorld;
    }

    @Override
    public void setIncludeWorld(boolean includeWorld) {
        this.includeWorld = includeWorld;
    }

    @Override
    public String toString() {
        return "BuriedMineBlock: " + material.toString() +
                " (world: " + world + ", include: " + includeWorld +
                ", depth: " + depth + ", explosion: " + explosion + ")";
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        depth = Math.abs(depth);

        this.depth = depth;
    }

    public boolean isExplosion() {
        return explosion;
    }

    public void setExplosion(boolean explosion) {
        this.explosion = explosion;
    }
}
