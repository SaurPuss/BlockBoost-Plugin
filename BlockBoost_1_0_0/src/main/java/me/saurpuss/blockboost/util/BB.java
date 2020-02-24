package me.saurpuss.blockboost.util;

public enum BB {
    BOUNCE( "bounce-blocks"),
    COMMAND("command-blocks"),
    POTION("potion-blocks"),
    SPEED("speed-blocks");

    private String configSection;

    BB(String configSection) {
        this.configSection = configSection;
    }

    public String section() {
        return configSection;
    }
}
