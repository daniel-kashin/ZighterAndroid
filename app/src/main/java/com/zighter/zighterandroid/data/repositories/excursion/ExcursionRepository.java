package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.excursion.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.excursion.Excursion;
import com.zighter.zighterandroid.data.entities.excursion.ServiceExcursion;
import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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

    @NonNull
    public Single<List<BoughtExcursion>> getBoughtExcursions() {
        //return excursionService.getBoughtExcursions();

        return Single.fromCallable(() -> {
            List<BoughtExcursion> list = new ArrayList<>();
            list.add(new BoughtExcursion("1", "Kixbox", "Owner of kixbox", "Moscow"));
            list.add(new BoughtExcursion("2", "Brandshop", "Owner of Branshop", "Moscow"));
            return list;
        });
    }

    @NonNull
    public Observable<DownloadStatus> downloadExcursion(@NonNull String excursionUuid) {
        return Observable.create(source -> {
            try {
                source.onNext(null);

                source.onComplete();
            } catch (Throwable t) {
                source.onError(t);
            }
        });
    }
}
