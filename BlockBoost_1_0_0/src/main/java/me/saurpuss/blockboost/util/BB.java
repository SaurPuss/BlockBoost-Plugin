package me.saurpuss.blockboost.util;

import com.google.common.base.Enums;

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
