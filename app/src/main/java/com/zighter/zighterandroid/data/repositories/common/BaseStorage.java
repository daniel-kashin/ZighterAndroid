package com.zighter.zighterandroid.data.repositories.common;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.StorIOSQLite;

public abstract class BaseStorage {
    @NonNull
    private StorIOSQLite storIOSQLite;

    public BaseStorage(@NonNull StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    @NonNull
    protected StorIOSQLite getSqLite() {
        return storIOSQLite;
    }
}
