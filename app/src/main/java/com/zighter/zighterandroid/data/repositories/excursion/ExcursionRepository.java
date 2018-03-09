package com.zighter.zighterandroid.data.repositories.excursion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.pushtorefresh.storio3.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResults;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionJob;
import com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationContract;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.entities.mapper.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.excursion.Excursion;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.file.FileHelper;
import com.zighter.zighterandroid.data.job_manager.JobManagerWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

import static com.zighter.zighterandroid.data.job_manager.JobManagerEventsBroadcastReceiver.Event;

import static com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus.DownloadStatus;

public class ExcursionRepository {
    @NonNull
    private final ExcursionStorage excursionStorage;
    @NonNull
    private final ExcursionService excursionService;
    @NonNull
    private final ExcursionMapper excursionMapper;
    @NonNull
    private final JobManagerWrapper jobManagerWrapper;
    @NonNull
    private final FileHelper fileHelper;
    @NonNull
    private final Context applicationContext;

    public ExcursionRepository(@NonNull Context applicationContext,
                               @NonNull ExcursionService excursionService,
                               @NonNull ExcursionStorage excursionStorage,
                               @NonNull ExcursionMapper excursionMapper,
                               @NonNull JobManagerWrapper jobManagerWrapper,
                               @NonNull FileHelper fileHelper) {
        this.applicationContext = applicationContext;
        this.excursionService = excursionService;
        this.excursionStorage = excursionStorage;
        this.excursionMapper = excursionMapper;
        this.jobManagerWrapper = jobManagerWrapper;
        this.fileHelper = fileHelper;
    }

    @NonNull
    public Single<Excursion> getExcursion() {
        return excursionService.getExcursion()
                .map(excursionMapper::fromService);
    }

    @NonNull
    public Single<List<BoughtExcursionWithStatus>> getBoughtExcursions() {
        return Single.zip(
                excursionService.getBoughtExcursionsOrException(),
                excursionStorage.getBoughtExcursions(),

                (pair, listFromStorageOptional) -> {
                    List<ServiceBoughtExcursion> listFromService = pair.first;
                    List<StorageBoughtExcursion> listFromStorage = listFromStorageOptional.get();

                    if (listFromService == null) {
                        if (listFromStorage == null || listFromStorage.isEmpty()) {
                            throw pair.second;
                        } else {
                            return excursionMapper.fromStorage(listFromStorage, fileHelper);
                        }
                    }

                    List<BoughtExcursion> boughtExcursions = excursionMapper
                            .fromServiceAndStorage(listFromService, listFromStorage, fileHelper);

                    DeleteResult deleteResult = excursionStorage
                            .deleteBoughtExcursions()
                            .blockingGet();
                    PutResults<StorageBoughtExcursion> results = excursionStorage
                            .saveBoughtExcursions(excursionMapper.toStorage(boughtExcursions))
                            .blockingGet();

                    return boughtExcursions;
                })
                .map(list -> {
                    List<BoughtExcursionWithStatus> result = new ArrayList<>(list.size());
                    for (BoughtExcursion boughtExcursion : list) {
                        DownloadStatus downloadStatus;
                        if (jobManagerWrapper.isAdded(boughtExcursion.getUuid())) {
                            downloadStatus = DownloadStatus.DOWNLOADING;
                        } else if (boughtExcursion.isFullySaved()) {
                            downloadStatus = DownloadStatus.DOWNLOADED;
                        } else {
                            downloadStatus = DownloadStatus.IDLE;
                        }
                        result.add(new BoughtExcursionWithStatus(boughtExcursion, downloadStatus));
                    }
                    return result;
                });
    }

    @NonNull
    public Observable<Pair<String, Event>> subscribeOnDownloadExcursionEvents() {
        return jobManagerWrapper.subscribeOnJobEvents();
    }

    public void addDownloadExcursionJob(@NonNull BoughtExcursion boughtExcursion) {
        DownloadExcursionJob job = new DownloadExcursionJob(boughtExcursion);
        jobManagerWrapper.getJobManager().addJobInBackground(job);
    }

    public void removeDownloadExcursionJob(@NonNull BoughtExcursion boughtExcursion) {
        DownloadExcursionNotificationContract.cancel(applicationContext, boughtExcursion);
    }

    @NonNull
    public Observable<DownloadStatus> downloadExcursion(@NonNull String excursionUuid) {
        return Observable.create(source -> {
            try {
                Excursion excursion = excursionService.getExcursion()
                        .map(excursionMapper::fromService)
                        .blockingGet();
                source.onNext(null);

                source.onComplete();
            } catch (Throwable t) {
                source.onError(t);
            }
        });
    }
}
