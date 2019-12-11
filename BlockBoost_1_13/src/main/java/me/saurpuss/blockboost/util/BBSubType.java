package me.saurpuss.blockboost.util;

public enum BBSubType {
    SPEED_ADDITION, SPEED_MULTIPLIER;

    public static BBSubType getByName(String name) {
        if (name.equalsIgnoreCase(SPEED_ADDITION.toString()))
            return SPEED_ADDITION;
        else if (name.equalsIgnoreCase(SPEED_MULTIPLIER.toString()))
            return SPEED_MULTIPLIER;
        else
            return null;
    }
}
