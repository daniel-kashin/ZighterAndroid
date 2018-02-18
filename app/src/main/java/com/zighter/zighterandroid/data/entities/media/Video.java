package com.zighter.zighterandroid.data.entities.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Video extends DrawableMedia {
    public Video(@NonNull String url,
                 @Nullable String path,
                 @Nullable String name,
                 @Nullable String description) {
        super(url, path, name, description);
    }
}
