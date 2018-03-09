package com.zighter.zighterandroid.dagger.module.singleton;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.zighter.zighterandroid.data.entities.mapper.ExcursionMapper;
import com.zighter.zighterandroid.data.file.FileHelper;
import com.zighter.zighterandroid.data.job_manager.JobManagerWrapper;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionService;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class ExcursionModule {
    @Singleton
    @Provides
    ExcursionService providePathsService(OkHttpClient okHttpClient) {
        return new ExcursionService(okHttpClient);
    }

    @Singleton
    @Provides
    ExcursionStorage providePathsStorage(StorIOSQLite storIOSQLite) {
        return new ExcursionStorage(storIOSQLite);
    }

    @Singleton
    @Provides
    ExcursionMapper provideExcursionMapper() {
        return new ExcursionMapper();
    }

    @Singleton
    @Provides
    ExcursionRepository provideExcursionRepository(Context context,
                                                   ExcursionService excursionService,
                                                   ExcursionStorage excursionStorage,
                                                   ExcursionMapper excursionMapper,
                                                   JobManagerWrapper jobManager,
                                                   FileHelper fileHelper) {
        return new ExcursionRepository(context,
                                       excursionService,
                                       excursionStorage,
                                       excursionMapper,
                                       jobManager,
                                       fileHelper);
    }
}
