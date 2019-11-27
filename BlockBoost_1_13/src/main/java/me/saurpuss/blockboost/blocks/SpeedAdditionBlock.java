package me.saurpuss.blockboost.blocks;

import me.saurpuss.blockboost.util.AbstractBlock;
import org.bukkit.Material;

public class SpeedAdditionBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private double addition;
        private int duration;

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

        public Builder withAddition(double addition) {
            this.addition = addition;

            return this;
        }

        public Builder withDuration(int duration) {
            duration = Math.abs(duration);

            this.duration = duration;

            return this;
        }

        public SpeedAdditionBlock build() {
            SpeedAdditionBlock block = new SpeedAdditionBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.addition = this.addition;
            block.duration = this.duration;

            return block;
        }
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private double addition;
    private int duration;

    private SpeedAdditionBlock() {}

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

    @Override
    public String toString() {
        return "SpeedMultiplierBlock: " + material.toString() +
                " (world: " + world + ", include: " + includeWorld +
                ", addition: " + addition + ", duration: " + duration + " (seconds))";
    }

    public double getAddition() {
        return addition;
    }

    public void setAddition(double addition) {
        this.addition = addition;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        duration = Math.abs(duration);

        this.duration = duration;
    }
}
