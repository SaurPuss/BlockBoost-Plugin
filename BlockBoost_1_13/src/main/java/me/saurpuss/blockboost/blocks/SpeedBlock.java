package me.saurpuss.blockboost.blocks;

import me.saurpuss.blockboost.BlockBoost;
import me.saurpuss.blockboost.util.AbstractBlock;
import me.saurpuss.blockboost.util.BBSubType;
import me.saurpuss.blockboost.util.SpeedResetTask;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SpeedBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private BBSubType subtype;
        private float amount;
        private float cap;
        private long duration;
        private long cooldown;

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

        public Builder withType(BBSubType subtype) {
            this.subtype = subtype;

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

        public Builder withDuration(long duration) {
            this.duration = Math.abs(duration);

            return this;
        }

        public Builder withCooldown(long cooldown) {
            this.cooldown = Math.abs(cooldown);

            return this;
        }

        public SpeedBlock build() {
            SpeedBlock block = new SpeedBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.subType = this.subtype;
            block.amount = this.amount;
            block.cap = this.cap;
            block.duration = this.duration;
            block.cooldown = this.cooldown;

            return block;
        }
    }

    private static HashMap<UUID, Float> speedReset;
    private static HashMap<UUID, Long> onCooldown;

    static {
        speedReset = new HashMap<>();
        onCooldown = new HashMap<>();

        // TODO make additions and removals for the maps
        // TODO synchronize
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private BBSubType subType;
    private float amount;
    private float cap;
    private long duration;
    private long cooldown;

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

    public BBSubType getSubType() {
        return subType;
    }

    public void setSubType(BBSubType subType) {
        this.subType = subType;
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
        if (cap < -1.0 || cap > 1.0) cap = 1.0f;

        this.cap = cap;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = Math.abs(duration);
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = Math.abs(cooldown);
    }

    @Override
    public String toColorString() {
        return ChatColor.GREEN + material.toString() + ChatColor.GRAY + " (world: " + world +
                ", include: " + includeWorld + ", subtype: " + subType + ", amount: " + amount +
                ", cap: " + cap + ", duration: " + duration + " seconds, cooldown: " +
                cooldown + " ticks)";
    }

    @Override
    public String toString() {
        return material.toString() + " (world: " + world + ", include: " + includeWorld +
                ", subtype: " + subType + ", amount: " + amount + ", cap: " + cap + ", duration: " +
                duration + " seconds, cooldown: " + cooldown + " ticks)";
    }

    @Override
    public void activate(Player player) {
        final float playerSpeed = player.getWalkSpeed();
        float resultSpeed;

        // Calculate result speed
        if (getSubType() == BBSubType.SPEED_MULTIPLIER) resultSpeed = playerSpeed * getAmount();
        else resultSpeed = playerSpeed + getAmount();
        if (resultSpeed >= 1.0f || resultSpeed > getCap()) resultSpeed = getCap();
        // TODO negative cap?

        player.setWalkSpeed(resultSpeed);

        // TODO map stuff
        onCooldown.put(player.getUniqueId(), System.currentTimeMillis() + (getCooldown() * 20));

        // Create a reset task to go back to original speed
        if (!onCooldown.containsKey(player.getUniqueId())) {
            speedReset.put(player.getUniqueId(), playerSpeed);
            new SpeedResetTask(player, playerSpeed).runTaskLater( // TODO replace plugin reference
                    BlockBoost.getPlugin(BlockBoost.class), getDuration() * 20);
        }
    }
}
