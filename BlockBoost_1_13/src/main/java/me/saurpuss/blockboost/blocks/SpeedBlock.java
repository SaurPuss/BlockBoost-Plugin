package me.saurpuss.blockboost.blocks;

import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BBSubType;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class SpeedBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private BBSubType type;
        private float amount;
        private float cap;
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

        public Builder withType(BBSubType type) {
            this.type = type;

            return this;
        }

        public Builder withAmount(float amount) {
            this.amount = amount;

            return this;
        }

        public Builder withCap(float cap) {
            this.cap = cap;

            return this;
        }

        public Builder withDuration(int duration) {
            this.duration = Math.abs(duration);

            return this;
        }

        public Builder withCooldown(int cooldown) {
            this.cooldown = Math.abs(cooldown);

            return this;
        }

        public SpeedBlock build() {
            SpeedBlock block = new SpeedBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.type = this.type;
            block.amount = this.amount;
            block.cap = this.cap;
            block.duration = this.duration;
            block.cooldown = this.cooldown;

            return block;
        }
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private BBSubType type;
    private float amount;
    private float cap;
    private int duration;
    private int cooldown;

    private SpeedBlock() {}

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

    public BBSubType getType() {
        return type;
    }

    public void setType(BBSubType type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getCap() {
        return cap;
    }

    public void setCap(float cap) {
        if (cap < -1.0 || cap > 1.0)
            cap = 1.0f;

        this.cap = cap;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = Math.abs(duration);
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = Math.abs(cooldown);
    }

    @Override
    public String toColorString() {
        return ChatColor.GREEN + material.toString() + ChatColor.GRAY + " (world: " + world +
                ", include: " + includeWorld + ", type: " + type + ", amount: " + amount +
                ", cap: " + cap + ", duration: " + duration + " seconds, cooldown: " +
                cooldown + " ticks)";
    }

    @Override
    public String toString() {
        return material.toString() + " (world: " + world + ", include: " + includeWorld +
                ", type: " + type + ", amount: " + amount + ", cap: " + cap + ", duration: " +
                duration + " seconds, cooldown: " + cooldown + " ticks)";
    }
}
