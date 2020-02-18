package me.saurpuss.blockboost.util;

public enum BB {
    BOUNCE("bounceBlocks.yml", "bounce"),
    SPEED("speedBlocks.yml", "speed"),
    POTION("potionBlocks.yml", "potion");

    private String fileName;
    private String configSection;

    BB(String fileName, String configSection) {
        this.fileName = fileName;
        this.configSection = configSection;
    }

    public BB getByName(String name) {
        switch (name.toUpperCase()) {
            case "BOUNCE": return BOUNCE;
            case "SPEED": return SPEED;
            case "POTION": return POTION;
            default: return null;
        }
    }

    public String file() {
        return fileName;
    }

    public String section() {
        return configSection;
    }
}
