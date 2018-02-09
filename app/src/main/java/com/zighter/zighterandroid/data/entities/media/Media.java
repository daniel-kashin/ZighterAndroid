package com.zighter.zighterandroid.data.entities.media;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

public class Media {
    @Nullable
    private final String path;
    @NonNull
    private final String url;

    public Media(@NonNull String url, @Nullable String path) {
        this.url = url;
        this.path = path;
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
    public Uri getUriFromPath() {
        return path != null ? Uri.fromFile(new File(path)) : null;
    }

    public boolean isCached() {
        return path != null;
    }
}
