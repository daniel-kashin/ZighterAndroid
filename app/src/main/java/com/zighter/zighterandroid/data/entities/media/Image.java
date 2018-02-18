package com.zighter.zighterandroid.data.entities.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Image extends DrawableMedia {
    public Image(@NonNull String url,
                 @Nullable String path,
                 @Nullable String name,
                 @Nullable String description) {
        super(url, path, name, description);
    }
}
