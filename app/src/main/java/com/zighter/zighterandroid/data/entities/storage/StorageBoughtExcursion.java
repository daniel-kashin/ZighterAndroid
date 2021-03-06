package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;
import com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract;

import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_ID;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IMAGE_URL;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_GUIDE_AVAILABLE;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_GUIDE_SAVED;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_MEDIA_AVAILABLE;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_MEDIA_SAVED;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_ROUTE_AVAILABLE;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_IS_ROUTE_SAVED;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_LOCATION;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_NAME;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_OWNER;
import static com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract.COLUMN_OWNER_UUID;
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

    @StorIOSQLiteColumn(name = COLUMN_OWNER_UUID)
    String ownerUuid;

    @StorIOSQLiteColumn(name = COLUMN_IMAGE_URL)
    String imageUrl;

    @StorIOSQLiteColumn(name = COLUMN_IS_GUIDE_AVAILABLE)
    boolean isGuideAvailable;

    @StorIOSQLiteColumn(name = COLUMN_IS_MEDIA_AVAILABLE)
    boolean isMediaAvailable;

    @StorIOSQLiteColumn(name = COLUMN_IS_ROUTE_AVAILABLE)
    boolean isRouteAvailable;

    @StorIOSQLiteColumn(name = COLUMN_IS_ROUTE_SAVED)
    boolean isRouteSaved;

    @StorIOSQLiteColumn(name = COLUMN_IS_GUIDE_SAVED)
    boolean isGuideSaved;

    @StorIOSQLiteColumn(name = COLUMN_IS_MEDIA_SAVED)
    boolean isMediaSaved;

    StorageBoughtExcursion() {
    }

    public StorageBoughtExcursion(@NonNull String uuid,
                                  @NonNull String name,
                                  @NonNull String location,
                                  @NonNull String owner,
                                  @NonNull String ownerUuid,
                                  @NonNull String imageUrl,
                                  boolean isGuideAvailable,
                                  boolean isMediaAvailable,
                                  boolean isRouteAvailable,
                                  boolean isRouteSaved,
                                  boolean isGuideSaved,
                                  boolean isMediaSaved) {
        this.uuid = uuid;
        this.name = name;
        this.location = location;
        this.imageUrl = imageUrl;
        this.isGuideAvailable = isGuideAvailable;
        this.isMediaAvailable = isMediaAvailable;
        this.isRouteAvailable = isRouteAvailable;
        this.isRouteSaved = isRouteSaved;
        this.isGuideSaved = isGuideSaved;
        this.isMediaSaved = isMediaSaved;
        this.owner = owner;
        this.ownerUuid = ownerUuid;
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

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    @NonNull
    public String getOwner() {
        return owner;
    }

    @NonNull
    public String getOwnerUuid() {
        return ownerUuid;
    }

    public boolean isGuideAvailable() {
        return isGuideAvailable;
    }

    public boolean isMediaAvailable() {
        return isMediaAvailable;
    }

    public boolean isRouteAvailable() {
        return true;
    }

    public boolean isRouteSaved() {
        return isRouteSaved;
    }

    public boolean isGuideSaved() {
        return isGuideSaved;
    }

    public boolean isMediaSaved() {
        return isMediaSaved;
    }

    public void setRouteSaved(boolean routeSaved) {
        isRouteSaved = routeSaved;
    }

    public void setGuideSaved(boolean guideSaved) {
        isGuideSaved = guideSaved;
    }

    public void setMediaSaved(boolean mediaSaved) {
        isMediaSaved = mediaSaved;
    }
}
