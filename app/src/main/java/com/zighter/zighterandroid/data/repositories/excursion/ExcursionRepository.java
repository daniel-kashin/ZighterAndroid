package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.birbit.android.jobqueue.JobStatus;
import com.zighter.zighterandroid.data.download_excursion.DownloadExcursionJob;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.entities.excursion.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.excursion.Excursion;
import com.zighter.zighterandroid.data.job_manager.JobManagerWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static com.zighter.zighterandroid.data.entities.excursion.BoughtExcursionWithStatus.DownloadStatus;

public class ExcursionRepository {
    @NonNull
    private final ExcursionStorage excursionStorage;
    @NonNull
    private final ExcursionService excursionService;
    @NonNull
    private final ExcursionMapper excursionMapper;
    @NonNull
    private final JobManagerWrapper jobManagerWrapper;

    public ExcursionRepository(@NonNull ExcursionService excursionService,
                               @NonNull ExcursionStorage excursionStorage,
                               @NonNull ExcursionMapper excursionMapper,
                               @NonNull JobManagerWrapper jobManagerWrapper) {
        this.excursionService = excursionService;
        this.excursionStorage = excursionStorage;
        this.excursionMapper = excursionMapper;
        this.jobManagerWrapper = jobManagerWrapper;
    }

    @NonNull
    public Single<Excursion> getExcursion() {
        return excursionService.getExcursion()
                .map(excursionMapper::fromService);
    }

    @NonNull
    public Single<List<BoughtExcursionWithStatus>> getBoughtExcursions() {
        //return excursionService.getBoughtExcursions();

        return Single.fromCallable(() -> {
            List<BoughtExcursion> list = new ArrayList<>();
            list.add(new BoughtExcursion("1", "Kixbox", "Owner of kixbox", "Moscow"));
            list.add(new BoughtExcursion("2", "Brandshop", "Owner of Branshop", "Moscow"));
            return list;
        }).map(list -> {
            List<BoughtExcursionWithStatus> result = new ArrayList<>(list.size());
            for (BoughtExcursion boughtExcursion : list) {
                DownloadStatus downloadStatus;
                if (jobManagerWrapper.isRunning(boughtExcursion.getUuid())) {
                    downloadStatus = DownloadStatus.DOWNLOADING;
                } else {
                    downloadStatus = DownloadStatus.IDLE;
                }
                result.add(new BoughtExcursionWithStatus(boughtExcursion, downloadStatus));
            }
            return result;
        });
    }

    @NonNull
    public Observable<Pair<String, JobManagerWrapper.Action>> subscribeOnDownloadExcursionEvents() {
        return jobManagerWrapper.subscribeOnDownloadExcursionEvents();
    }

    @NonNull
    public Completable addDownloadExcursionJob(@NonNull BoughtExcursion boughtExcursion) {
        return Completable.fromAction(() -> {
            if (!jobManagerWrapper.isRunning(boughtExcursion.getUuid())) {
                DownloadExcursionJob job = new DownloadExcursionJob(boughtExcursion);
                jobManagerWrapper.getJobManager().addJob(job);
            }
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
