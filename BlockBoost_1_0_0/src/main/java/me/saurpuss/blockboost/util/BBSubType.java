package me.saurpuss.blockboost.util;

public enum BBSubType {
    SPEED_ADDITION, SPEED_MULTIPLIER;

    public static BBSubType getByName(String name) {
        switch (name.toUpperCase()) {
            case "SPEED_ADDITION":
            case "SPEEDADDITION":
            case "ADDITION":
                return SPEED_ADDITION;
            case "SPEED_MULTIPLIER":
            case "SPEEDMULTIPLIER":
            case "MULTIPLIER":
                return SPEED_MULTIPLIER;
            default:
                return null;
        }
    }
}
