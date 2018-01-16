package com.zighter.zighterandroid.dagger.module.singleton;

import com.zighter.zighterandroid.data.repositories.excursions.ExcursionsRepository;
import com.zighter.zighterandroid.data.repositories.excursions.ExcursionsService;
import com.zighter.zighterandroid.data.repositories.excursions.ExcursionsStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PathsModule {
    @Singleton
    @Provides
    ExcursionsService providePathsService(OkHttpClient okHttpClient) {
        return new ExcursionsService(okHttpClient);
    }

    @Singleton
    @Provides
    ExcursionsStorage providePathsStorage() {
        return new ExcursionsStorage();
    }

    @Singleton
    @Provides
    ExcursionsRepository providePathsRepository(ExcursionsService excursionsService, ExcursionsStorage excursionsStorage) {
        return new ExcursionsRepository(excursionsService, excursionsStorage);
    }
}
