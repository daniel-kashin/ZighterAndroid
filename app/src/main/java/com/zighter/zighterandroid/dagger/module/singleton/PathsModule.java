package com.zighter.zighterandroid.dagger.module.singleton;

import com.zighter.zighterandroid.data.entities.excursion.ExcursionMapper;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionService;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PathsModule {
    @Singleton
    @Provides
    ExcursionService providePathsService(OkHttpClient okHttpClient) {
        return new ExcursionService(okHttpClient);
    }

    @Singleton
    @Provides
    ExcursionStorage providePathsStorage() {
        return new ExcursionStorage();
    }

    @Singleton
    @Provides
    ExcursionMapper provideExcursionMapper() {
        return new ExcursionMapper();
    }

    @Singleton
    @Provides
    ExcursionRepository providePathsRepository(ExcursionService excursionService,
                                               ExcursionStorage excursionStorage,
                                               ExcursionMapper excursionMapper) {
        return new ExcursionRepository(excursionService, excursionStorage, excursionMapper);
    }
}
