package me.saurpuss.blockboost.blocks;

import me.saurpuss.blockboost.util.AbstractBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class SpeedMultiplierBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private float defaultSpeed;
        private float speedMultiplier;
        private float speedCap;
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

        public Builder withDefaultSpeed(float defaultSpeed) {
            if (defaultSpeed < 0 || defaultSpeed > 1)
                defaultSpeed = 0.2f;

            this.defaultSpeed = defaultSpeed;

            return this;
        }

        public Builder withSpeedMultiplier(float speedMultiplier) {
            speedMultiplier = Math.abs(speedMultiplier);

            this.speedMultiplier = speedMultiplier;

            return this;
        }

        public Builder withCap(float speedCap) {
            speedCap = Math.abs(speedCap);
            if (speedCap > 1.0)
                speedCap = 1.0f;

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
    private float defaultSpeed;
    private float speedMultiplier;
    private float speedCap;
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

    public float getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(float defaultSpeed) {
        if (defaultSpeed < 0 || defaultSpeed > 1)
            defaultSpeed = 0.2f;
        this.defaultSpeed = defaultSpeed;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        speedMultiplier = Math.abs(speedMultiplier);

        this.speedMultiplier = speedMultiplier;
    }

    public float getSpeedCap() {
        return speedCap;
    }

    public void setSpeedCap(float speedCap) {
        speedCap = Math.abs(speedCap);
        if (speedCap > 1.0)
            speedCap = 1.0f;

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

    @Override
    public String toColorString() {
        return ChatColor.GREEN + material.toString() + ChatColor.GRAY + " (world: " + world +
                ", include: " + includeWorld + ", default: " + defaultSpeed + ", multiplier: " +
                speedMultiplier + ", cap: " + speedCap + ", duration: " + duration +
                " seconds, cooldown: " + cooldown + " ticks)";
    }

    @Override
    public String toString() {
        return material.toString() + " (world: " + world + ", include: " + includeWorld +
                ", default: " + defaultSpeed + ", multiplier: " + speedMultiplier + ", cap: " +
                speedCap + ", duration: " + duration + " seconds" + ", cooldown: " + cooldown +
                " ticks)";
    }
}
