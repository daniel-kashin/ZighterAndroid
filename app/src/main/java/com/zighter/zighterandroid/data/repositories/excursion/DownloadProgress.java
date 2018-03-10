package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;

public class DownloadProgress {
    @NonNull
    private final Type type;
    private final int currentPosition;
    private final int count;

    public DownloadProgress(@NonNull Type type) {
        this.type = type;
        this.count = 0;
        this.currentPosition = -1;
    }

    public DownloadProgress(@NonNull Type type, int currentPosition, int count) {
        this.type = type;
        this.currentPosition = currentPosition;
        this.count = count;
    }

    @NonNull
    public Type getType() {
        return type;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int getCount() {
        return count;
    }

    public enum Type {
        DATABASE,
        MAP,
        SIGHT
    }
}