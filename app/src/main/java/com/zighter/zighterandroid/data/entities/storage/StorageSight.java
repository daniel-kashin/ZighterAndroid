package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.zighter.zighterandroid.data.database.StorageSightContract;

public class StorageSight {
    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_ID)
    Long id;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_UUID)
    String uuid;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_EXCURSION_UUID)
    String excursionUuid;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_DESCRIPTION)
    String description;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_LONGITUDE)
    double longitude;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_LATITUDE)
    double latitude;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_NAME)
    String name;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_TYPE)
    String type;

    StorageSight() {
    }

    public StorageSight(@NonNull String uuid,
                        @NonNull String excursionUuid,
                        @NonNull String name,
                        @NonNull String type,
                        @Nullable String description,
                        double longitude,
                        double latitude) {
        this.uuid = uuid;
        this.excursionUuid = excursionUuid;
        this.name = name;
        this.type = type;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getExcursionUuid() {
        return excursionUuid;
    }
}
