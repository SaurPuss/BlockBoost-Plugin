package me.saurpuss.blockboost.util.blockbuilders;

import me.saurpuss.blockboost.util.util.AbstractBlock;
import org.bukkit.Material;

public class BounceBlock extends AbstractBlock {

    public static class BounceBuilder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private int height;
        private boolean normalize;

        public BounceBuilder(Material material) {
            this.material = material;
        }

        public BounceBuilder withWorld(String world) {
            this.world = world;

            return this;
        }

        public BounceBuilder withIncludeWorld(boolean includeWorld) {
            this.includeWorld = includeWorld;

            return this;
        }

        public BounceBuilder withHeight(int height) {
            this.height = height;

            return this;
        }

        public BounceBuilder withNormalize(boolean normalize) {
            this.normalize = normalize;

            return this;
        }

        public BounceBlock build() {
            BounceBlock block = new BounceBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.height = this.height;
            block.normalize = this.normalize;

            return block;
        }
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private int height;
    private boolean normalize;

    private BounceBlock() {}

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    protected void setMaterial(Material material) {
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
