package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;
import com.zighter.zighterandroid.data.database.StoragePointContract;

@StorIOSQLiteType(table = StoragePointContract.TABLE_NAME)
public class StoragePoint {
    @StorIOSQLiteColumn(name = StoragePointContract.COLUMN_ID, key = true)
    Long id = null;

    @StorIOSQLiteColumn(name = StoragePointContract.COLUMN_PATH_UUID)
    String pathUuid;

    @StorIOSQLiteColumn(name = StoragePointContract.COLUMN_EXCURSION_UUID)
    String excursionUuid;

    @StorIOSQLiteColumn(name = StoragePointContract.COLUMN_LONGITUDE)
    double longitude;

    @StorIOSQLiteColumn(name = StoragePointContract.COLUMN_LATITUDE)
    double latitude;

    StoragePoint() {
    }

    public StoragePoint(@NonNull String pathUuid,
                        @NonNull String excursionUuid,
                        double longitude,
                        double latitude) {
        this.pathUuid = pathUuid;
        this.excursionUuid = excursionUuid;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @NonNull
    public String getPathUuid() {
        return pathUuid;
    }

    @NonNull
    public String getExcursionUuid() {
        return excursionUuid;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
