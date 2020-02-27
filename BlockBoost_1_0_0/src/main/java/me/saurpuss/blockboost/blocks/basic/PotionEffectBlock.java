package me.saurpuss.blockboost.blocks.basic;

import me.saurpuss.blockboost.util.AbstractBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private PotionEffectType effectType;
        private int duration;
        private int amplifier;

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

        public Builder withEffectType(PotionEffectType effectType) {
            this.effectType = effectType;

            return this;
        }

        public Builder withDuration(int duration) {
            this.duration = duration * 20;

            return this;
        }

        public Builder withAmplifier(int amplifier) {
            this.amplifier = amplifier;

            return this;
        }

        public PotionEffectBlock build() {
            PotionEffectBlock block = new PotionEffectBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.effectType = this.effectType;
            block.duration = this.duration;
            block.amplifier = this.amplifier;

            return block;
        }
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private PotionEffectType effectType;
    private int duration;
    private int amplifier;

    private PotionEffectBlock() {}

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

    public PotionEffectType getEffectType() {
        return effectType;
    }

    public void setEffectType(PotionEffectType effectType) {
        this.effectType = effectType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        duration = 20 * Math.abs(duration);

        this.duration = duration;
    }

    public int getAmplifier() {
        return amplifier;
    }

    public void setAmplifier(int amplifier) {
        amplifier = Math.abs(amplifier);

        this.amplifier = amplifier;
    }

    @Override
    public String toColorString() {
        return ChatColor.GREEN + material.toString() + ChatColor.GRAY + " (world: " + world +
                ", include: " + includeWorld + ", effect: " + effectType + ", duration: " +
                (duration / 20) + " seconds, amplifier: " + amplifier + ")";
    }

    @Override
    public String toString() {
        return material.toString() + " (world: " + world + ", include: " + includeWorld +
                ", effect: " + effectType + ", duration: " + (duration / 20) + " seconds, " +
                "amplifier: " + amplifier + ")";
    }

    @Override
    public void activate(Player player) {
        player.addPotionEffect(new PotionEffect(effectType, duration, amplifier));
    }
}
