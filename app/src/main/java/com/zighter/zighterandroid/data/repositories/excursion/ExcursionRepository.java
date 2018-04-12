package com.zighter.zighterandroid.data.repositories.excursion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.util.Pair;

import com.zighter.zighterandroid.data.entities.media.Audio;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;
import com.zighter.zighterandroid.data.entities.presentation.Guide;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursionBindResponse;
import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.service.ServicePoint;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchExcursions;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchSort;
import com.zighter.zighterandroid.data.entities.service.ServiceToken;
import com.zighter.zighterandroid.data.entities.storage.StorageExcursion;
import com.zighter.zighterandroid.data.entities.storage.StorageGuide;
import com.zighter.zighterandroid.data.entities.storage.StoragePath;
import com.zighter.zighterandroid.data.entities.storage.StoragePoint;
import com.zighter.zighterandroid.data.entities.storage.StorageMedia;
import com.zighter.zighterandroid.data.entities.storage.StorageSight;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;
import com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionJob;
import com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationContract;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.entities.mapper.ExcursionMapper;
import com.zighter.zighterandroid.data.entities.presentation.Excursion;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.job_manager.JobManagerWrapper;
import com.zighter.zighterandroid.data.mapbox.MapboxHelper;
import com.zighter.zighterandroid.data.repositories.token.TokenStorage;
import com.zighter.zighterandroid.util.Optional;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

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
    private final TokenStorage tokenStorage;
    @NonNull
    private final Context applicationContext;

    public ExcursionRepository(@NonNull Context applicationContext,
                               @NonNull ExcursionService excursionService,
                               @NonNull ExcursionStorage excursionStorage,
                               @NonNull ExcursionMapper excursionMapper,
                               @NonNull JobManagerWrapper jobManagerWrapper,
                               @NonNull TokenStorage tokenStorage) {
        this.applicationContext = applicationContext;
        this.excursionService = excursionService;
        this.excursionStorage = excursionStorage;
        this.excursionMapper = excursionMapper;
        this.jobManagerWrapper = jobManagerWrapper;
        this.tokenStorage = tokenStorage;
    }

    @NonNull
    public Single<String> login(@NonNull String username, @NonNull String password) {
        return excursionService.login(username, password)
                .map(ServiceToken::getToken)
                .doOnSuccess(token -> {
                    excursionStorage.deleteAllData();
                    tokenStorage.saveToken(token);
                    tokenStorage.saveLogin(username);
                })
                .doOnError(error -> {
                    excursionStorage.deleteAllData();
                    tokenStorage.saveToken(null);
                    tokenStorage.saveLogin(null);
                });
    }

    @NonNull
    public Single<String> register(@NonNull String email,
                                   @NonNull String firstName,
                                   @NonNull String lastName,
                                   @NonNull String password,
                                   @NonNull String username) {
        return excursionService.register(email, firstName, lastName, password, username)
                .map(ServiceToken::getToken)
                .doOnSuccess(token -> {
                    excursionStorage.deleteAllData();
                    tokenStorage.saveToken(token);
                    tokenStorage.saveLogin(username);
                })
                .doOnError(error -> {
                    excursionStorage.deleteAllData();
                    tokenStorage.saveToken(null);
                    tokenStorage.saveLogin(null);
                });
    }

    @NonNull
    public Single<String> getLogin() {
        return tokenStorage.getLoginSingle();
    }

    @NonNull
    public Completable logOut() {
        return excursionStorage.deleteAllData()
                .doOnComplete(() -> {
                    excursionStorage.deleteAllData();
                    tokenStorage.saveToken(null);
                    tokenStorage.saveLogin(null);
                });
    }

    @NonNull
    public Single<ServiceExcursionBindResponse> bindExcursionRoute(@NonNull String excursionUuid) {
        return tokenStorage.getTokenSingle()
                .flatMap(token -> excursionService.bindExcursionRoute(excursionUuid, token))
                .doOnError(error -> {
                    if (error instanceof ServerNotAuthorizedException) {
                        excursionStorage.deleteAllData();
                        tokenStorage.saveToken(null);
                        tokenStorage.saveLogin(null);
                    }
                });
    }

    @NonNull
    public Single<List<ServiceSearchExcursion>> searchExcursions(@NonNull String request,
                                                                 @NonNull ServiceSearchSort sort) {
        return excursionService.searchExcursions(request, sort)
                .map(excursions -> excursions.tryGetValidOrThrowException().getExcursions());
    }

    @NonNull
    public Single<Excursion> getExcursion(@NonNull String excursionUuid) {
        return tokenStorage.getTokenSingle()
                .flatMap(token -> excursionService.getExcursion(excursionUuid, token))
                .map(excursionMapper::fromService)
                .doOnSuccess(excursion -> saveExcursionToStorage(excursion).blockingAwait())
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof ServerNotAuthorizedException) {
                        excursionStorage.deleteAllData();
                        tokenStorage.saveToken(null);
                        tokenStorage.saveLogin(null);
                        return Single.error(throwable);
                    }

                    return getExcursionFromStorage(excursionUuid)
                            .flatMap(fromStorageOptional -> {
                                Excursion fromStorage = fromStorageOptional.get();
                                if (fromStorage != null) {
                                    return Single.just(fromStorage);
                                }

                                return Single.error(throwable);
                            });
                });
    }

    @NonNull
    public Single<Guide> getGuide(@NonNull String ownerUuid) {
        return tokenStorage.getTokenSingle()
                .flatMap(token -> excursionService.getGuide(ownerUuid, token))
                .map(excursionMapper::fromService)
                .doOnSuccess(guide -> excursionStorage.saveGuide(excursionMapper.toStorage(guide)).blockingGet())
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof ServerNotAuthorizedException) {
                        excursionStorage.deleteAllData();
                        tokenStorage.saveToken(null);
                        tokenStorage.saveLogin(null);
                        return Single.error(throwable);
                    }

                    return excursionStorage.getGuide(ownerUuid)
                            .flatMap(fromStorageOptional -> {
                                StorageGuide guide = fromStorageOptional.get();
                                if (guide != null) {
                                    return Single.just(excursionMapper.fromStorage(guide));
                                }

                                return Single.error(throwable);
                            });
                });
    }

    @NonNull
    public Single<List<BoughtExcursionWithStatus>> getBoughtExcursions() {
        return Single.zip(
                tokenStorage.getTokenSingle().flatMap(excursionService::getBoughtExcursionsOrException),

                excursionStorage.getBoughtExcursions(),

                (listFromServiceOrException, listFromStorageOptional) -> {
                    List<ServiceBoughtExcursion> listFromService = listFromServiceOrException.first;
                    List<StorageBoughtExcursion> listFromStorage = listFromStorageOptional.get();

                    if (listFromService == null) {
                        if (listFromServiceOrException.second instanceof ServerNotAuthorizedException) {
                            excursionStorage.deleteAllData();
                            tokenStorage.saveToken(null);
                            tokenStorage.saveLogin(null);
                            throw listFromServiceOrException.second;
                        }

                        if (listFromStorage == null || listFromStorage.isEmpty()) {
                            throw listFromServiceOrException.second;
                        } else {
                            return excursionMapper.fromStorage(listFromStorage);
                        }
                    }

                    List<BoughtExcursion> boughtExcursions =
                            excursionMapper.fromServiceAndStorage(listFromService, listFromStorage);

                    excursionStorage.deleteBoughtExcursions().blockingGet();
                    excursionStorage.saveBoughtExcursions(excursionMapper.toStorage(boughtExcursions)).blockingGet();

                    return boughtExcursions;
                })
                .map(list -> {
                    List<BoughtExcursionWithStatus> result = new ArrayList<>(list.size());
                    for (BoughtExcursion boughtExcursion : list) {
                        DownloadStatus downloadStatus;
                        if (jobManagerWrapper.isAdded(boughtExcursion.getUuid())) {
                            downloadStatus = DownloadStatus.DOWNLOADING;
                        } else if (boughtExcursion.isSavedAllAvailable()) {
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
                // get excursion
                source.onNext(new DownloadProgress(DownloadProgress.Type.JSON));
                if (source.isDisposed()) {
                    return;
                }

                StorageBoughtExcursion boughtExcursion = excursionStorage
                        .getBoughtExcursion(excursionUuid)
                        .blockingGet()
                        .get();

                if (boughtExcursion == null) {
                    source.onError(new IllegalStateException());
                    return;
                }

                if (boughtExcursion.isGuideAvailable() && !boughtExcursion.isGuideSaved()) {
                    Guide guide = getGuide(excursionUuid).blockingGet();
                    boughtExcursion.setGuideSaved(true);
                    excursionStorage.saveBoughtExcursion(boughtExcursion).blockingGet();
                }

                if (boughtExcursion.isRouteAvailable() && !boughtExcursion.isRouteSaved()) {
                    Excursion excursion = getExcursion(excursionUuid).blockingGet();

                    // download map
                    source.onNext(new DownloadProgress(DownloadProgress.Type.MAP));
                    if (source.isDisposed()) {
                        return;
                    }

                    MapboxHelper.createOfflineRegion(applicationContext, excursion, new MapboxHelper.MapboxRegionCreateListener() {
                        private static final String TAG = "MapboxCreateListener";
                        private final Object lock = new Object();

                        @Override
                        public boolean isDisposed() {
                            synchronized (lock) {
                                return source.isDisposed();
                            }
                        }

                        @Override
                        public synchronized void onProgressChanged(int currentPosition, int requiredCount) {
                            synchronized (lock) {
                                Log.d(TAG, "onProgressChanged(" + currentPosition + "/" + requiredCount + ")");
                                source.onNext(new DownloadProgress(DownloadProgress.Type.MAP, currentPosition, requiredCount));
                            }
                        }

                        @Override
                        public void onComplete() {
                            synchronized (lock) {
                                Log.d(TAG, "onComplete");
                                source.onNext(new DownloadProgress(DownloadProgress.Type.JSON));

                                boughtExcursion.setRouteSaved(true);
                                excursionStorage.saveBoughtExcursion(boughtExcursion).blockingGet();

                                source.onComplete();
                            }
                        }

                        @Override
                        public void onError() {
                            synchronized (lock) {
                                Log.d(TAG, "onError");
                                if (!source.isDisposed()) {
                                    source.onError(new NetworkUnavailableException(null));
                                }
                            }
                        }
                    });
                } else {
                    source.onComplete();
                }
            } catch (Throwable t) {
                source.onError(t);
            }
        });
    }

    @NonNull
    @VisibleForTesting
    public Single<Optional<Excursion>> getExcursionFromStorage(@NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            Excursion result = null;

            // get excursion
            StorageExcursion excursion = excursionStorage.getExcursion(excursionUuid).blockingGet().get();

            if (excursion != null) {
                // get sights
                List<Sight> sights = new ArrayList<>();
                List<StorageSight> storageSights = excursionStorage.getSights(excursionUuid).blockingGet().get();
                if (storageSights != null) {
                    for (StorageSight storageSight : storageSights) {
                        // get medias
                        List<Media> medias = new ArrayList<>();
                        List<StorageMedia> storageMedias = excursionStorage.getMedias(storageSight.getUuid(), excursionUuid).blockingGet().get();
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
                                             medias,
                                             storageSight.getTimetable(),
                                             storageSight.getPhone(),
                                             storageSight.getAddress(),
                                             storageSight.getWebsite()));
                    }
                }

                // get paths
                List<ServicePath> paths = new ArrayList<>();
                List<StoragePath> storagePaths = excursionStorage.getPaths(excursion.getUuid()).blockingGet().get();
                if (storagePaths != null) {
                    for (StoragePath storagePath : storagePaths) {
                        // get points
                        List<ServicePoint> points = new ArrayList<>();
                        List<StoragePoint> storagePoints = excursionStorage.getPoints(storagePath.getUuid(), excursionUuid).blockingGet().get();
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

            return Optional.Companion.of(result);
        });
    }

    @NonNull
    @VisibleForTesting
    public Completable saveExcursionToStorage(@NonNull Excursion excursion) {
        return Completable.fromAction(() -> {
            // save paths
            excursionStorage.deletePaths(excursion.getUuid()).blockingGet();
            for (int i = 0; i < excursion.getPathSize(); ++i) {
                ServicePath path = excursion.getPathAt(i);
                List<StoragePoint> storagePoints = new ArrayList<>(path.getPointSize());
                for (int j = 0; j < path.getPointSize(); ++j) {
                    ServicePoint point = path.getPointAt(j);
                    storagePoints.add(new StoragePoint(path.getUuid(), excursion.getUuid(), point.getLongitude(), point.getLatitude()));
                }

                excursionStorage.deletePoints(path.getUuid(), excursion.getUuid()).blockingGet();
                excursionStorage.savePoints(storagePoints).blockingGet();
                excursionStorage.savePath(new StoragePath(path.getUuid(), excursion.getUuid())).blockingGet();
            }

            // save sights
            excursionStorage.deleteSights(excursion.getUuid()).blockingGet();
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
                                                          excursion.getUuid(),
                                                          type,
                                                          media.getTitle(),
                                                          media.getDescription()));
                    }
                }

                excursionStorage.deleteMedias(sight.getUuid(), excursion.getUuid()).blockingGet();
                excursionStorage.saveMedias(storageMedia).blockingGet();
                excursionStorage.saveSight(new StorageSight(sight.getUuid(),
                                                            excursion.getUuid(),
                                                            sight.getName(),
                                                            sight.getType(),
                                                            sight.getDescription(),
                                                            sight.getLongitude(),
                                                            sight.getLatitude(),
                                                            sight.getTimetable(),
                                                            sight.getPhone(),
                                                            sight.getAddress(),
                                                            sight.getWebsite())).blockingGet();
            }

            // save excursion
            StorageExcursion storageExcursion = new StorageExcursion(excursion.getName(),
                                                                     excursion.getUuid(),
                                                                     excursion.getWestNorthMapBound().getLongitude(),
                                                                     excursion.getWestNorthMapBound().getLatitude(),
                                                                     excursion.getEastSouthMapBound().getLongitude(),
                                                                     excursion.getEastSouthMapBound().getLatitude(),
                                                                     excursion.getMinMapZoom(),
                                                                     excursion.getMaxMapZoom());
            excursionStorage.saveExcursion(storageExcursion).blockingGet();
        });
    }
}
