package me.saurpuss.blockboost.util;

public enum BB {
    BOUNCE("bounceBlocks.yml", "bounce"),
    SPEED("speedBlocks.yml", null),
    SPEED_ADDITION("speedBlocks.yml", "addition"),
    SPEED_MULTIPLIER("speedBlocks.yml", "multiplier"),
    POTION("potionBlocks.yml", "potion"),
    LANDMINE("landmineBlocks.yml", null),
    BURIED_MINE("landmineBlocks.yml", "buried"),
    TRIP_MINE("landmineBlocks.yml", "trip");

    private String fileName;
    private String configSection;

    BB(String fileName, String configSection) {
        this.fileName = fileName;
        this.configSection = configSection;
    }

    public String file() {
        return fileName;
    }

    public String section() {
        return configSection;
    }
}
