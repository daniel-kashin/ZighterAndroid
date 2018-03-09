package com.zighter.zighterandroid.data.database;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursionGetResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursionStorIOSQLiteDeleteResolver;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursionStorIOSQLitePutResolver;

public class DatabaseFactory {
    private DatabaseFactory() {
    }

    public static StorIOSQLite create(@NonNull Context context) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new DatabaseOpenHelper(context))
                .addTypeMapping(StorageBoughtExcursion.class,
                                SQLiteTypeMapping.<StorageBoughtExcursion>builder()
                                        .putResolver(new StorageBoughtExcursionStorIOSQLitePutResolver())
                                        .getResolver(new StorageBoughtExcursionGetResolver())
                                        .deleteResolver(new StorageBoughtExcursionStorIOSQLiteDeleteResolver())
                                        .build())
                .build();
    }
}
