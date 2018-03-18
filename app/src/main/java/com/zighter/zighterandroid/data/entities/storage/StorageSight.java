package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;
import com.zighter.zighterandroid.data.database.StorageSightContract;

@StorIOSQLiteType(table = StorageSightContract.TABLE_NAME)
public class StorageSight {
    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_ID, key = true)
    Long id = null;

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

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_TIMETABLE)
    String timetable;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_PHONE)
    String phone;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_ADDRESS)
    String address;

    @StorIOSQLiteColumn(name = StorageSightContract.COLUMN_WEBSITE)
    String website;

    StorageSight() {
    }

    public StorageSight(@NonNull String uuid,
                        @NonNull String excursionUuid,
                        @NonNull String name,
                        @NonNull String type,
                        @Nullable String description,
                        double longitude,
                        double latitude,
                        @Nullable String timetable,
                        @Nullable String phone,
                        @Nullable String address,
                        @Nullable String website) {
        this.uuid = uuid;
        this.excursionUuid = excursionUuid;
        this.name = name;
        this.type = type;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timetable = timetable;
        this.phone = phone;
        this.address = address;
        this.website = website;
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

    @Nullable
    public String getTimetable() {
        return timetable;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    @Nullable
    public String getWebsite() {
        return website;
    }
}
