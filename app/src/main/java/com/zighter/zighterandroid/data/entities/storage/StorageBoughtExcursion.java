package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;
import com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract;

import java.security.acl.Owner;

import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_ID;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IMAGE_URL;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_FULLY_SAVED;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_GUIDE_AVAILABLE;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_MEDIA_AVAILABLE;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_ROUTE_AVAILABLE;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_LOCATION;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_NAME;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_OWNER;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_UUID;

@StorIOSQLiteType(table = StorageBoughtExcursionContract.TABLE_NAME)
public class StorageBoughtExcursion {
    @StorIOSQLiteColumn(name = COLUMN_ID, key = true)
    Long id = null;

    @StorIOSQLiteColumn(name = COLUMN_UUID)
    String uuid;

    @StorIOSQLiteColumn(name = COLUMN_NAME)
    String name;

    @StorIOSQLiteColumn(name = COLUMN_LOCATION)
    String location;

    @StorIOSQLiteColumn(name = COLUMN_OWNER)
    String owner;

    @StorIOSQLiteColumn(name = COLUMN_IMAGE_URL)
    @Nullable
    String imageUrl;

    @StorIOSQLiteColumn(name = COLUMN_IS_GUIDE_AVAILABLE)
    boolean isGuideAvailable;

    @StorIOSQLiteColumn(name = COLUMN_IS_MEDIA_AVAILABLE)
    boolean isMediaAvailable;

    @StorIOSQLiteColumn(name = COLUMN_IS_ROUTE_AVAILABLE)
    boolean isRouteAvailable;

    @StorIOSQLiteColumn(name = COLUMN_IS_FULLY_SAVED)
    boolean isFullySaved;

    StorageBoughtExcursion() {
    }

    public StorageBoughtExcursion(@NonNull String uuid,
                                  @NonNull String name,
                                  @NonNull String location,
                                  @NonNull String owner,
                                  @Nullable String imageUrl,
                                  boolean isGuideAvailable,
                                  boolean isMediaAvailable,
                                  boolean isRouteAvailable,
                                  boolean isFullySaved) {
        this.uuid = uuid;
        this.name = name;
        this.location = location;
        this.imageUrl = imageUrl;
        this.isGuideAvailable = isGuideAvailable;
        this.isMediaAvailable = isMediaAvailable;
        this.isRouteAvailable = isRouteAvailable;
        this.isFullySaved = isFullySaved;
        this.owner = owner;
    }

    public long getId() {
        return id != null ? id : -1;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    public String getOwner() {
        return owner;
    }

    public boolean isGuideAvailable() {
        return isGuideAvailable;
    }

    public boolean isMediaAvailable() {
        return isMediaAvailable;
    }

    public boolean isRouteAvailable() {
        return isRouteAvailable;
    }

    public boolean isFullySaved() {
        return isFullySaved;
    }
}
