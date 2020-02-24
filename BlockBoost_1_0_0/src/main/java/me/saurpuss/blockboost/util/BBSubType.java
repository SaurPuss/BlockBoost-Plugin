package me.saurpuss.blockboost.util;

import com.google.common.base.Enums;

public enum BBSubType {
    SPEED_ADDITION, SPEED_MULTIPLIER;

    public static BBSubType getByName(String name) {
        String value = "";
        switch (name.toUpperCase()) {
            case "MULTIPLIER":
            case "SPEED_MULTIPLIER":
            case "SPEEDMULTIPLIER":
                value = "SPEED_MULTIPLIER";
                break;
            case "ADDITION":
            case "SPEED_ADDITION":
            case "SPEEDADDITION":
                value = "SPEED_ADDITION";
        }

        return Enums.getIfPresent(BBSubType.class, value).orNull();
    }
}
