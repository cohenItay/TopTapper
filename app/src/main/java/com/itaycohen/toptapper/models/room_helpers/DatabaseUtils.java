package com.itaycohen.toptapper.models.room_helpers;

import com.itaycohen.toptapper.ui.models.Level;

public class DatabaseUtils {

    public static String getColumnForLevel(Level level) {
        String column;
        switch (level) {
            case BASIC:
                column = "beginner";
                break;
            case INTERMEDIATE:
                column = "intermediate";
                break;
            case EXPERT:
                column = "expert";
                break;
            default:
                throw new IllegalArgumentException("Must be a level");
        }
        return column;
    }
}
