package me.saurpuss.blockboost.util;

import com.google.common.base.Enums;

public enum BB {

    BOUNCE("bounce-blocks"),
    POTION("potion-blocks");

    private String configSection;

    BB(String configSection) {
        this.configSection = configSection;
    }

    public String section() {
        return configSection;
    }

    public static BB getIfPresent(String name) {
        return Enums.getIfPresent(BB.class, name.toUpperCase()).orNull();
    }

}
