package me.saurpuss.blockboost.blocks.basic;

import me.saurpuss.blockboost.util.AbstractBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ExplodingBlock extends AbstractBlock {

    public static class Builder {
        private Material material;
        private String world;
        private boolean includeWorld;
        private float power;
        private boolean setFire;
        private boolean breakBlocks;
        private double playerDamage;
        private boolean playerKill;

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

        public Builder withPower(float power) {
            this.power = power;

            return this;
        }

        public Builder withSetFire(boolean setFire) {
            this.setFire = setFire;

            return this;
        }

        public Builder withBreakBlocks(boolean breakBlocks) {
            this.breakBlocks = breakBlocks;

            return this;
        }

        public Builder withPlayerDamage(double playerDamage) {
            this.playerDamage = playerDamage;

            return this;
        }

        public Builder withPlayerKill(boolean playerKill) {
            this.playerKill = playerKill;

            return this;
        }

        public ExplodingBlock build() {
            ExplodingBlock block = new ExplodingBlock();
            block.material = this.material;
            block.world = this.world;
            block.includeWorld = this.includeWorld;
            block.power = this.power;
            block.setFire = this.setFire;
            block.breakBlocks = this.breakBlocks;
            block.playerDamage = this.playerDamage;
            block.playerKill = this.playerKill;

            return block;
        }
    }

    private Material material;
    private String world;
    private boolean includeWorld;
    private float power;
    private boolean setFire;
    private boolean breakBlocks;
    private double playerDamage;
    private boolean playerKill;


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

    public float getPower() {
        return power;
    }

    public void setPower(float power) {
        this.power = power;
    }

    public boolean isSetFire() {
        return setFire;
    }

    public void setSetFire(boolean setFire) {
        this.setFire = setFire;
    }

    public boolean isBreakBlocks() {
        return breakBlocks;
    }

    public void setBreakBlocks(boolean breakBlocks) {
        this.breakBlocks = breakBlocks;
    }

    public double getPlayerDamage() {
        return playerDamage;
    }

    public void setPlayerDamage(double playerDamage) {
        this.playerDamage = playerDamage;
    }

    public boolean getPlayerKill() {
        return playerKill;
    }

    public void setPlayerKill(boolean playerKill) {
        this.playerKill = playerKill;
    }

    @Override
    public String toColorString() {
        return ChatColor.GREEN + material.toString() + ChatColor.GRAY + " (world: " + world +
                ", include: " + includeWorld + ")";
    }

    @Override
    public String toString() {
        return material.toString() + " (world: " + world + ", include: " + includeWorld + ")";
    }

    @Override
    public void activate(Player player) {
        Location location = player.getLocation();
        World world = player.getWorld();

        world.createExplosion(location.getX(), location.getY(), location.getZ(),
                power, setFire, breakBlocks); // TODO for newer spigot it's location instead of XYZ

        if (playerDamage > 0.0) {
            double result = player.getHealth() - playerDamage;

            if (result < 0) {
                if (playerKill)
                    result = 0;
                else
                    result = 0.5;
            }

            player.setHealth(result);
        }
    }
}
