package com.zighter.zighterandroid.data.file;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.zighter.zighterandroid.data.entities.media.Image;

public class FileHelper {
    @NonNull
    private final Context context;

    public FileHelper(@NonNull Context context) {
        this.context = context;
    }

    @Nullable
    @WorkerThread
    public Image getImage(@Nullable String imageUri) {
        return imageUri != null ? new Image(imageUri, null, null, null) : null;
    }

    @Nullable
    @WorkerThread
    public Image saveImage(@Nullable String imageUri) {
        return imageUri != null ? new Image(imageUri, null, null, null) : null;
    }
}
