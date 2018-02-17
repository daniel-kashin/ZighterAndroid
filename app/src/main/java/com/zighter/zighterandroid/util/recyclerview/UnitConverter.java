package com.zighter.zighterandroid.util.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

public final class UnitConverter {

    private UnitConverter() {
    }

    public static int dpToPx(@NonNull Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return Math.round(dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(@NonNull Context context, float px) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return Math.round(px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToSp(@NonNull Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return Math.round(px / scaledDensity);
    }
}