package me.saurpuss.blockboost.util.blocks;

import me.saurpuss.blockboost.util.util.AbstractBlock;
import org.bukkit.Material;

public class SpeedMultiplierBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private double defaultSpeed;
        private double speedMultiplier;
        private double speedCap;
        private int duration;
        private int cooldown;

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

        public Builder withDefaultSpeed(double defaultSpeed) {
            if (defaultSpeed < 0 || defaultSpeed > 1)
                defaultSpeed = 0.2;

            this.defaultSpeed = defaultSpeed;

            return this;
        }

        public Builder withSpeedMultiplier(double speedMultiplier) {
            speedMultiplier = Math.abs(speedMultiplier);

            this.speedMultiplier = speedMultiplier;

            return this;
        }

        public Builder withCap(double speedCap) {
            speedCap = Math.abs(speedCap);
            if (speedCap > 1.0)
                speedCap = 1.0;

            this.speedCap = speedCap;

            return this;
        }

        public Builder withDuration(int duration) {
            duration = Math.abs(duration);

            this.duration = duration;

            return this;
        }

        public Builder withCooldown(int cooldown) {
            cooldown = Math.abs(cooldown);

            this.cooldown = cooldown;

            return this;
        }

        public SpeedMultiplierBlock build() {
            SpeedMultiplierBlock block = new SpeedMultiplierBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.defaultSpeed = this.defaultSpeed;
            block.speedMultiplier = this.speedMultiplier;
            block.speedCap = this.speedCap;
            block.duration = this.duration;
            block.cooldown = this.cooldown;

            return block;
        }
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private double defaultSpeed;
    private double speedMultiplier;
    private double speedCap;
    private int duration;
    private int cooldown;

    private SpeedMultiplierBlock() {}

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
                ", default: " + defaultSpeed + ", multiplier: " + speedMultiplier +
                ", cap: " + speedCap + ", duration: " + duration + " (seconds)" +
                ", cooldown: " + cooldown + " (ticks))";
    }

    public double getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(double defaultSpeed) {
        if (defaultSpeed < 0 || defaultSpeed > 1)
            defaultSpeed = 0.2;
        this.defaultSpeed = defaultSpeed;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        speedMultiplier = Math.abs(speedMultiplier);

        this.speedMultiplier = speedMultiplier;
    }

    public double getSpeedCap() {
        return speedCap;
    }

    public void setSpeedCap(double speedCap) {
        speedCap = Math.abs(speedCap);
        if (speedCap > 1.0)
            speedCap = 1.0;

        this.speedCap = speedCap;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        duration = Math.abs(duration);

        this.duration = duration;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        cooldown = Math.abs(cooldown);

        this.cooldown = cooldown;
    }
}
