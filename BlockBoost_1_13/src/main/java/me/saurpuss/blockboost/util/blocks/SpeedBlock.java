package me.saurpuss.blockboost.util.blocks;

import org.bukkit.Material;

public class SpeedBlock {

    private Material material;
    private float defaultSpeed;
    private float speedMultiplier;
    private float speedCap;
    private long duration;

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public float getDefaultSpeed() {
        return defaultSpeed;
    }

    public void setDefaultSpeed(float defaultSpeed) {
        this.defaultSpeed = defaultSpeed;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }

    public float getSpeedCap() {
        return speedCap;
    }

    public void setSpeedCap(float speedCap) {
        this.speedCap = speedCap;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String toString() {
        return "material: " + material.toString() + " speed: " + speedMultiplier + " duration: " + duration;
    }
}
