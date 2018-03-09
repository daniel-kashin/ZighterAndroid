package com.zighter.zighterandroid.util;

import android.support.annotation.NonNull;

public class BooleanHelper {
    public static int toInt(boolean value) {
        return value ? 1 : 0;
    }

    public static boolean toBoolean(int value) {
        return value != 0;
    }

    public static boolean toBoolean(@NonNull String value) {
        return value.equals(String.valueOf(true));
    }

    @NonNull
    public static String toString(boolean value) {
        return String.valueOf(value);
    }
}
