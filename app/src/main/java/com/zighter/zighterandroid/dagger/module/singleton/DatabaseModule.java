package com.zighter.zighterandroid.dagger.module.singleton;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.zighter.zighterandroid.data.database.DatabaseFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    @Singleton
    @Provides
    StorIOSQLite provideStorIOSQLite(Context context) {
        return DatabaseFactory.create(context);
    }
}
