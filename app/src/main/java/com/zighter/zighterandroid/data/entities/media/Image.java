package com.zighter.zighterandroid.data.entities.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

public class Image extends Media {
    public Image(@NonNull String url, @Nullable String path) {
        super(url, path);
    }
}
