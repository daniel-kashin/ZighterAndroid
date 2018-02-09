package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.excursion.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.excursion.Excursion;
import com.zighter.zighterandroid.data.entities.excursion.ServiceExcursion;
import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;

import io.reactivex.Single;

public class ExcursionRepository {
    @NonNull
    private final ExcursionStorage excursionStorage;
    @NonNull
    private final ExcursionService excursionService;
    @NonNull
    private final ExcursionMapper excursionMapper;

    public ExcursionRepository(@NonNull ExcursionService excursionService,
                               @NonNull ExcursionStorage excursionStorage,
                               @NonNull ExcursionMapper excursionMapper) {
        this.excursionService = excursionService;
        this.excursionStorage = excursionStorage;
        this.excursionMapper = excursionMapper;
    }

    @NonNull
    public Single<Excursion> getExcursion() {
        return excursionService.getExcursion()
                .map(excursionMapper::fromService);
    }
}
