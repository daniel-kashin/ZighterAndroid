package com.zighter.zighterandroid.data.entities.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DrawableMedia extends Media {
    DrawableMedia(@NonNull String url,
                  @Nullable String path,
                  @Nullable String title,
                  @Nullable String description) {
        super(url, path, title, description);
    }
}
