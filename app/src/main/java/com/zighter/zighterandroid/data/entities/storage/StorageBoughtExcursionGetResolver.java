package com.zighter.zighterandroid.data.entities.storage;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.operations.get.DefaultGetResolver;

import java.lang.Override;

import static com.zighter.zighterandroid.util.BooleanHelper.toBoolean;

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

public class StorageBoughtExcursionGetResolver extends DefaultGetResolver<StorageBoughtExcursion> {
    @Override
    @NonNull
    public StorageBoughtExcursion mapFromCursor(@NonNull StorIOSQLite storIOSQLite,
                                                @NonNull Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        String imageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URL));
        boolean isFullySaved = toBoolean(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_FULLY_SAVED)));
        boolean isMediaAvailable = toBoolean(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_MEDIA_AVAILABLE)));
        boolean isGuideAvailable = toBoolean(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_GUIDE_AVAILABLE)));
        boolean isRouteAvailable = toBoolean(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ROUTE_AVAILABLE)));
        String uuid = cursor.getString(cursor.getColumnIndex(COLUMN_UUID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));
        String owner = cursor.getString(cursor.getColumnIndex(COLUMN_OWNER));

        if (uuid == null || name == null || location == null || owner == null) {
            throw new IllegalStateException();
        }

        return new StorageBoughtExcursion(id, uuid, name, location, owner, imageUrl, isGuideAvailable, isMediaAvailable, isRouteAvailable, isFullySaved);
    }
}
