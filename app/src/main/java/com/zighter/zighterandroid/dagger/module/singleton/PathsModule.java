package com.zighter.zighterandroid.dagger.module.singleton;

import com.zighter.zighterandroid.data.repositories.paths.PathsRepository;
import com.zighter.zighterandroid.data.repositories.paths.PathsService;
import com.zighter.zighterandroid.data.repositories.paths.PathsStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PathsModule {

    @Singleton
    @Provides
    PathsService providePathsService(OkHttpClient okHttpClient) {
        return new PathsService(okHttpClient);
    }

    @Singleton
    @Provides
    PathsStorage providePathsStorage() {
        return new PathsStorage();
    }

    @Singleton
    @Provides
    PathsRepository providePathsRepository(PathsService pathsService, PathsStorage pathsStorage) {
        return new PathsRepository(pathsService, pathsStorage);
    }

}
