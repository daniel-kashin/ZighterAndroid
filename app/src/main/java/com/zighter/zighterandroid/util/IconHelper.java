package com.zighter.zighterandroid.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.zighter.zighterandroid.R;

public class IconHelper {

    @NonNull
    public static Icon getIcon(@NonNull Context context, @NonNull Type type) {
        int drawableRes;
        switch (type) {
            case SIGHT:
                drawableRes = R.drawable.ic_navigation_black;
                break;
            case ENDPOINT:
                drawableRes = R.drawable.ic_sight_black;
                break;
            case CHECKED_SIGHT:
                drawableRes = R.drawable.ic_checked_sight;
                break;
            default: throw new IllegalStateException("Unknown icon type");
        }

        return IconFactory.getInstance(context).fromResource(drawableRes);
    }

    public enum Type {
        ENDPOINT,
        SIGHT,
        CHECKED_SIGHT
    }

}
