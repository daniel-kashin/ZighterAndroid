package com.zighter.zighterandroid.data.entities.media;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.Serializable;

public class Media implements Serializable {
    @NonNull
    private final String url;
    @Nullable
    private final String path;
    @Nullable
    private final String title;
    @Nullable
    private final String description;

    public Media(@NonNull String url,
                 @Nullable String path,
                 @Nullable String title,
                 @Nullable String description) {
        this.url = url;
        this.path = path;
        this.title = title;
        this.description = description;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @Nullable
    public String getPath() {
        return path;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public Uri getUriFromPath() {
        return path != null ? Uri.fromFile(new File(path)) : null;
    }

    public boolean isCached() {
        return path != null;
    }
}
