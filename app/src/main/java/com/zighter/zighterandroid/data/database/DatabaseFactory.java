package com.zighter.zighterandroid.data.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursionStorIOSQLiteDeleteResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursionStorIOSQLiteGetResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursionStorIOSQLitePutResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageExcursion;
import com.zighter.zighterandroid.data.entities.storage.StorageExcursionStorIOSQLiteDeleteResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageExcursionStorIOSQLiteGetResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageExcursionStorIOSQLitePutResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageGuide;
import com.zighter.zighterandroid.data.entities.storage.StorageGuideStorIOSQLiteDeleteResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageGuideStorIOSQLiteGetResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageGuideStorIOSQLitePutResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageMedia;
import com.zighter.zighterandroid.data.entities.storage.StorageMediaStorIOSQLiteDeleteResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageMediaStorIOSQLiteGetResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageMediaStorIOSQLitePutResolver;
import com.zighter.zighterandroid.data.entities.storage.StoragePath;
import com.zighter.zighterandroid.data.entities.storage.StoragePathStorIOSQLiteDeleteResolver;
import com.zighter.zighterandroid.data.entities.storage.StoragePathStorIOSQLiteGetResolver;
import com.zighter.zighterandroid.data.entities.storage.StoragePathStorIOSQLitePutResolver;
import com.zighter.zighterandroid.data.entities.storage.StoragePoint;
import com.zighter.zighterandroid.data.entities.storage.StoragePointStorIOSQLiteDeleteResolver;
import com.zighter.zighterandroid.data.entities.storage.StoragePointStorIOSQLiteGetResolver;
import com.zighter.zighterandroid.data.entities.storage.StoragePointStorIOSQLitePutResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageSight;
import com.zighter.zighterandroid.data.entities.storage.StorageSightStorIOSQLiteDeleteResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageSightStorIOSQLiteGetResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageSightStorIOSQLitePutResolver;

public class DatabaseFactory {
    private DatabaseFactory() {
    }

    public static StorIOSQLite create(@NonNull Context context) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new DatabaseOpenHelper(context))
                .addTypeMapping(StorageBoughtExcursion.class,
                                SQLiteTypeMapping.<StorageBoughtExcursion>builder()
                                        .putResolver(new StorageBoughtExcursionStorIOSQLitePutResolver())
                                        .getResolver(new StorageBoughtExcursionStorIOSQLiteGetResolver())
                                        .deleteResolver(new StorageBoughtExcursionStorIOSQLiteDeleteResolver())
                                        .build())
                .addTypeMapping(StorageExcursion.class,
                                SQLiteTypeMapping.<StorageExcursion>builder()
                                        .putResolver(new StorageExcursionStorIOSQLitePutResolver())
                                        .getResolver(new StorageExcursionStorIOSQLiteGetResolver())
                                        .deleteResolver(new StorageExcursionStorIOSQLiteDeleteResolver())
                                        .build())
                .addTypeMapping(StorageMedia.class,
                                SQLiteTypeMapping.<StorageMedia>builder()
                                        .putResolver(new StorageMediaStorIOSQLitePutResolver())
                                        .getResolver(new StorageMediaStorIOSQLiteGetResolver())
                                        .deleteResolver(new StorageMediaStorIOSQLiteDeleteResolver())
                                        .build())
                .addTypeMapping(StoragePath.class,
                                SQLiteTypeMapping.<StoragePath>builder()
                                        .putResolver(new StoragePathStorIOSQLitePutResolver())
                                        .getResolver(new StoragePathStorIOSQLiteGetResolver())
                                        .deleteResolver(new StoragePathStorIOSQLiteDeleteResolver())
                                        .build())
                .addTypeMapping(StoragePoint.class,
                                SQLiteTypeMapping.<StoragePoint>builder()
                                        .putResolver(new StoragePointStorIOSQLitePutResolver())
                                        .getResolver(new StoragePointStorIOSQLiteGetResolver())
                                        .deleteResolver(new StoragePointStorIOSQLiteDeleteResolver())
                                        .build())
                .addTypeMapping(StorageSight.class,
                                SQLiteTypeMapping.<StorageSight>builder()
                                        .putResolver(new StorageSightStorIOSQLitePutResolver())
                                        .getResolver(new StorageSightStorIOSQLiteGetResolver())
                                        .deleteResolver(new StorageSightStorIOSQLiteDeleteResolver())
                                        .build())
                .addTypeMapping(StorageGuide.class,
                                SQLiteTypeMapping.<StorageGuide>builder()
                                        .putResolver(new StorageGuideStorIOSQLitePutResolver())
                                        .getResolver(new StorageGuideStorIOSQLiteGetResolver())
                                        .deleteResolver(new StorageGuideStorIOSQLiteDeleteResolver())
                                        .build())
                .build();
    }
}
