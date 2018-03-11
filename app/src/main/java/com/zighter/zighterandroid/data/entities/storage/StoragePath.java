package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;
import com.zighter.zighterandroid.data.database.StoragePathContract;
import com.zighter.zighterandroid.data.database.StoragePointContract;

import java.util.List;

@StorIOSQLiteType(table = StoragePathContract.TABLE_NAME)
public class StoragePath {
    @StorIOSQLiteColumn(name = StoragePathContract.COLUMN_ID, key = true)
    Long id = null;

    @StorIOSQLiteColumn(name = StoragePathContract.COLUMN_UUID)
    String uuid;

    @StorIOSQLiteColumn(name = StoragePathContract.COLUMN_EXCURSION_UUID)
    String excursionUuid;

    StoragePath() {
    }

    public StoragePath(@NonNull String uuid,
                       @NonNull String excursionUuid) {
        this.uuid = uuid;
        this.excursionUuid = excursionUuid;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @NonNull
    public String getExcursionUuid() {
        return excursionUuid;
    }
}
