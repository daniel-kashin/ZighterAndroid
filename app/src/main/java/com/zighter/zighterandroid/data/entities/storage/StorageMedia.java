package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;
import com.zighter.zighterandroid.data.database.StorageMediaContract;

@StorIOSQLiteType(table = StorageMediaContract.TABLE_NAME)
public class StorageMedia {
    @StorIOSQLiteColumn(name = StorageMediaContract.COLUMN_ID)
    Long id;

    @StorIOSQLiteColumn(name = StorageMediaContract.COLUMN_SIGHT_UUID)
    String sightUuid;

    @StorIOSQLiteColumn(name = StorageMediaContract.COLUMN_TITLE)
    String title;

    @StorIOSQLiteColumn(name = StorageMediaContract.COLUMN_URL)
    String url;

    @StorIOSQLiteColumn(name = StorageMediaContract.COLUMN_DESCRIPTION)
    String description;

    @StorIOSQLiteColumn(name = StorageMediaContract.COLUMN_TYPE)
    String typeName;

    StorageMedia() {
    }

    public StorageMedia(@NonNull String url,
                        @NonNull String sightUuid,
                        @NonNull Type type,
                        @Nullable String title,
                        @Nullable String description) {
        this.url = url;
        this.sightUuid = sightUuid;
        this.typeName = type.name();
        this.title = title;
        this.description = description;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public String getSightUuid() {
        return sightUuid;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @NonNull
    public Type getType() {
        return Type.valueOf(typeName);
    }

    public enum Type {
        IMAGE,
        VIDEO,
        AUDIO
    }
}
