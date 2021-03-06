package com.zighter.zighterandroid.data.entities.media;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Audio extends Media {
    public Audio(@NonNull String url,
                 @Nullable String path,
                 @Nullable String title,
                 @Nullable String description) {
        super(url, path, title, description);
    }
}
