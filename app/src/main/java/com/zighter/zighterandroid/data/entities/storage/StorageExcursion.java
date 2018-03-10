package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;
import com.zighter.zighterandroid.data.database.StorageExcursionContract;

@StorIOSQLiteType(table = StorageExcursionContract.TABLE_NAME)
public class StorageExcursion {

    @StorIOSQLiteColumn(name = StorageExcursionContract.COLUMN_ID, key = true)
    Long id;

    @SerializedName(value = StorageExcursionContract.COLUMN_UUID)
    String uuid;

    @StorIOSQLiteColumn(name = StorageExcursionContract.COLUMN_NAME)
    String name;

    @StorIOSQLiteColumn(name = StorageExcursionContract.COLUMN_WEST_BOUND)
    double westBound;

    @StorIOSQLiteColumn(name = StorageExcursionContract.COLUMN_NORTH_BOUND)
    double northBound;

    @StorIOSQLiteColumn(name = StorageExcursionContract.COLUMN_EAST_BOUND)
    double eastBound;

    @StorIOSQLiteColumn(name = StorageExcursionContract.COLUMN_SOUTH_BOUND)
    double southBound;

    @StorIOSQLiteColumn(name = StorageExcursionContract.COLUMN_MIN_ZOOM)
    int minZoom;

    @StorIOSQLiteColumn(name = StorageExcursionContract.COLUMN_MAX_ZOOM)
    int maxZoom;

    StorageExcursion() {
    }

    public StorageExcursion(@NonNull String name,
                            @NonNull String uuid,
                            double westBound,
                            double northBound,
                            double eastBound,
                            double southBound,
                            int minZoom,
                            int maxZoom) {
        this.name = name;
        this.uuid = uuid;
        this.westBound = westBound;
        this.northBound = northBound;
        this.eastBound = eastBound;
        this.southBound = southBound;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public double getWestBound() {
        return westBound;
    }

    public double getNorthBound() {
        return northBound;
    }

    public double getEastBound() {
        return eastBound;
    }

    public double getSouthBound() {
        return southBound;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }
}
