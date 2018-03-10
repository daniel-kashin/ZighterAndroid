package com.zighter.zighterandroid.data.repositories.excursion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.pushtorefresh.storio3.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResults;
import com.zighter.zighterandroid.data.entities.media.Audio;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.service.ServicePoint;
import com.zighter.zighterandroid.data.entities.storage.StorageExcursion;
import com.zighter.zighterandroid.data.entities.storage.StoragePath;
import com.zighter.zighterandroid.data.entities.storage.StoragePoint;
import com.zighter.zighterandroid.data.entities.storage.StorageMedia;
import com.zighter.zighterandroid.data.entities.storage.StorageSight;
import com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionJob;
import com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationContract;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.entities.mapper.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.presentation.Excursion;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.file.FileHelper;
import com.zighter.zighterandroid.data.job_manager.JobManagerWrapper;
import com.zighter.zighterandroid.util.Optional;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
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
    public Single<Excursion> getExcursion(@NonNull String excursionUuid) {
        return getExcursionFromStorage(excursionUuid)
                .flatMap(excursionOptional -> {
                    Excursion excursion = excursionOptional.get();
                    if (excursion != null) {
                        return Single.just(excursion);
                    } else {
                        return excursionService.getExcursion(excursionUuid)
                                .map(excursionMapper::fromService)
                                .doOnSuccess(this::saveExcursionToStorage);
                    }
                });
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
    public Observable<DownloadProgress> downloadExcursion(@NonNull String excursionUuid) {
        return Observable.create(source -> {
            try {
                source.onNext(new DownloadProgress(DownloadProgress.Type.DATABASE));
                Excursion excursion = getExcursion(excursionUuid).blockingGet();


                source.onNext(null);

                source.onComplete();
            } catch (Throwable t) {
                source.onError(t);
            }
        });
    }

    @NonNull
    private Single<Optional<Excursion>> getExcursionFromStorage(@NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            Excursion result = null;
            excursionStorage.beginTransaction();

            // get excursion
            StorageExcursion excursion = excursionStorage.getExcursion(excursionUuid).blockingGet().get();

            if (excursion != null) {
                // get sights
                List<Sight> sights = new ArrayList<>();
                List<StorageSight> storageSights = excursionStorage.getSights(excursion.getUuid()).blockingGet().get();
                if (storageSights != null) {
                    for (StorageSight storageSight : storageSights) {
                        // get medias
                        List<Media> medias = new ArrayList<>();
                        List<StorageMedia> storageMedias = excursionStorage.getMedias(storageSight.getUuid()).blockingGet().get();
                        if (storageMedias != null) {
                            for (StorageMedia storageMedia : storageMedias) {
                                if (storageMedia.getType() == StorageMedia.Type.VIDEO) {
                                    medias.add(new Video(storageMedia.getUrl(), null, storageMedia.getTitle(), storageMedia.getDescription()));
                                } else if (storageMedia.getType() == StorageMedia.Type.IMAGE) {
                                    medias.add(new Image(storageMedia.getUrl(), null, storageMedia.getTitle(), storageMedia.getDescription()));
                                } else if (storageMedia.getType() == StorageMedia.Type.AUDIO) {
                                    medias.add(new Audio(storageMedia.getUrl(), null, storageMedia.getTitle(), storageMedia.getDescription()));
                                }
                            }
                        }

                        sights.add(new Sight(storageSight.getUuid(),
                                             storageSight.getLongitude(),
                                             storageSight.getLatitude(),
                                             storageSight.getName(),
                                             storageSight.getType(),
                                             storageSight.getDescription(),
                                             medias));
                    }
                }

                // get paths
                List<ServicePath> paths = new ArrayList<>();
                List<StoragePath> storagePaths = excursionStorage.getPaths(excursion.getUuid()).blockingGet().get();
                if (storagePaths != null) {
                    for (StoragePath storagePath : storagePaths) {
                        // get points
                        List<ServicePoint> points = new ArrayList<>();
                        List<StoragePoint> storagePoints = excursionStorage.getPoints(storagePath.getUuid()).blockingGet().get();
                        if (storagePoints != null) {
                            for (StoragePoint storagePoint : storagePoints) {
                                points.add(new ServicePoint(storagePoint.getLongitude(), storagePoint.getLatitude()));
                            }
                        }

                        paths.add(new ServicePath(storagePath.getUuid(), points));
                    }
                }

                result = new Excursion(excursion.getUuid(),
                                       excursion.getName(),
                                       sights,
                                       paths,
                                       new ServicePoint(excursion.getWestBound(), excursion.getNorthBound()),
                                       new ServicePoint(excursion.getEastBound(), excursion.getSouthBound()),
                                       excursion.getMaxZoom(),
                                       excursion.getMinZoom());
            }

            excursionStorage.setTransactionSuccessful();
            return Optional.Companion.of(result);
        }).doOnError(throwable -> excursionStorage.endTransaction());
    }

    @NonNull
    private Completable saveExcursionToStorage(@NonNull Excursion excursion) {
        return Completable.fromAction(() -> {
            excursionStorage.beginTransaction();

            // save paths
            excursionStorage.deletePaths(excursion.getUuid());
            for (int i = 0; i < excursion.getPathSize(); ++i) {
                ServicePath path = excursion.getPathAt(i);
                List<StoragePoint> storagePoints = new ArrayList<>(path.getPointSize());
                for (int j = 0; j < path.getPointSize(); ++j) {
                    ServicePoint point = path.getPointAt(j);
                    storagePoints.add(new StoragePoint(path.getUuid(), point.getLongitude(), point.getLatitude()));
                }

                excursionStorage.deletePoints(path.getUuid());
                excursionStorage.savePoints(storagePoints);
                excursionStorage.savePath(new StoragePath(path.getUuid(), excursion.getUuid()));
            }

            // save sights
            excursionStorage.deleteSights(excursion.getUuid());
            for (int i = 0; i < excursion.getSightSize(); ++i) {
                Sight sight = excursion.getSightAt(i);

                List<Media> medias = sight.getMediasCopy(Media.class);
                List<StorageMedia> storageMedia = new ArrayList<>(medias.size());
                for (Media media : medias) {
                    StorageMedia.Type type = null;
                    if (media instanceof Video) {
                        type = StorageMedia.Type.VIDEO;
                    } else if (media instanceof Image) {
                        type = StorageMedia.Type.IMAGE;
                    } else if (media instanceof Audio) {
                        type = StorageMedia.Type.AUDIO;
                    }

                    if (type != null) {
                        storageMedia.add(new StorageMedia(media.getUrl(),
                                                          sight.getUuid(),
                                                          type,
                                                          media.getTitle(),
                                                          media.getDescription()));
                    }
                }

                excursionStorage.deleteMedias(sight.getUuid());
                excursionStorage.saveMedias(storageMedia);
                excursionStorage.saveSight(new StorageSight(sight.getUuid(),
                                                            excursion.getUuid(),
                                                            sight.getName(),
                                                            sight.getType(),
                                                            sight.getDescription(),
                                                            sight.getLongitude(),
                                                            sight.getLatitude()));
            }

            // save excursion
            excursionStorage.saveExcursion(new StorageExcursion(excursion.getName(),
                                                                excursion.getUuid(),
                                                                excursion.getWestNorthMapBound().getLongitude(),
                                                                excursion.getWestNorthMapBound().getLatitude(),
                                                                excursion.getEastSouthMapBound().getLongitude(),
                                                                excursion.getEastSouthMapBound().getLatitude(),
                                                                excursion.getMinMapZoom(),
                                                                excursion.getMaxMapZoom()));

            excursionStorage.endTransaction();
        }).doOnError(throwable -> excursionStorage.endTransaction());
    }
}
