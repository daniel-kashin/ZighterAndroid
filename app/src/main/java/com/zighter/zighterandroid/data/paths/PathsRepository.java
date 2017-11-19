package com.zighter.zighterandroid.data.paths;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.service.Excursion;

import io.reactivex.Single;

public class PathsRepository {

    @NonNull
    private final PathsStorage pathsStorage;
    @NonNull
    private final PathsService pathsService;

    public PathsRepository(@NonNull PathsService pathsService, @NonNull PathsStorage pathsStorage) {
        this.pathsService = pathsService;
        this.pathsStorage = pathsStorage;
    }

}
