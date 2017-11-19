package com.zighter.zighterandroid.dagger.module.singleton;

import com.zighter.zighterandroid.data.paths.PathsRepository;
import com.zighter.zighterandroid.data.paths.PathsService;
import com.zighter.zighterandroid.data.paths.PathsStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class PathsModule {

    @Singleton
    @Provides
    public PathsService providePathsService(OkHttpClient okHttpClient) {
        return new PathsService(okHttpClient);
    }

    @Singleton
    @Provides
    public PathsStorage providePathsStorage() {
        return new PathsStorage();
    }

    @Singleton
    @Provides
    public PathsRepository providePathsRepository(PathsService pathsService, PathsStorage pathsStorage) {
        return new PathsRepository(pathsService, pathsStorage);
    }

}
