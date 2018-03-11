package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio3.sqlite.operations.delete.DeleteResults;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio3.sqlite.queries.Query;
import com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract;
import com.zighter.zighterandroid.data.database.StorageExcursionContract;
import com.zighter.zighterandroid.data.database.StorageMediaContract;
import com.zighter.zighterandroid.data.database.StoragePathContract;
import com.zighter.zighterandroid.data.database.StoragePointContract;
import com.zighter.zighterandroid.data.database.StorageSightContract;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursion;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.entities.storage.StorageExcursion;
import com.zighter.zighterandroid.data.entities.storage.StoragePath;
import com.zighter.zighterandroid.data.entities.storage.StoragePoint;
import com.zighter.zighterandroid.data.entities.storage.StorageMedia;
import com.zighter.zighterandroid.data.entities.storage.StorageSight;
import com.zighter.zighterandroid.data.repositories.common.BaseStorage;
import com.zighter.zighterandroid.util.Optional;

import java.util.List;

import io.reactivex.Single;

@SuppressWarnings("UnusedReturnValue")
public class ExcursionStorage extends BaseStorage {

    public ExcursionStorage(@NonNull StorIOSQLite storIOSQLite) {
        super(storIOSQLite);
    }

    @NonNull
    Single<Optional<StorageExcursion>> getExcursion(@NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            StorageExcursion storageExcursion = getSqLite().get()
                    .object(StorageExcursion.class)
                    .withQuery(Query.builder()
                                       .table(StorageExcursionContract.TABLE_NAME)
                                       .where(StorageExcursionContract.COLUMN_UUID + " = ?")
                                       .whereArgs(excursionUuid)
                                       .build())
                    .prepare()
                    .executeAsBlocking();

            return Optional.Companion.of(storageExcursion);
        });
    }

    Single<PutResult> saveExcursion(@NonNull StorageExcursion storageExcursion) {
        return Single.fromCallable(() -> {
            return getSqLite().put()
                    .object(storageExcursion)
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<Optional<List<StorageSight>>> getSights(@NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return Optional.Companion.of(
                    getSqLite().get()
                            .listOfObjects(StorageSight.class)
                            .withQuery(Query.builder()
                                               .table(StorageSightContract.TABLE_NAME)
                                               .where(StorageSightContract.COLUMN_EXCURSION_UUID + " = ?")
                                               .whereArgs(excursionUuid)
                                               .build())
                            .prepare()
                            .executeAsBlocking()
            );
        });
    }

    @NonNull
    Single<PutResult> saveSight(@NonNull StorageSight storageSight) {
        return Single.fromCallable(() -> {
            return getSqLite().put()
                    .object(storageSight)
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<DeleteResult> deleteSights(@NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return getSqLite().delete()
                    .byQuery(DeleteQuery.builder()
                                     .table(StorageSightContract.TABLE_NAME)
                                     .where(StorageSightContract.COLUMN_EXCURSION_UUID + " = ?")
                                     .whereArgs(excursionUuid)
                                     .build())
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<Optional<List<StorageMedia>>> getMedias(@NonNull String sightUuid, @NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return Optional.Companion.of(
                    getSqLite().get()
                            .listOfObjects(StorageMedia.class)
                            .withQuery(Query.builder()
                                               .table(StorageMediaContract.TABLE_NAME)
                                               .where(StorageMediaContract.COLUMN_SIGHT_UUID + " = ? AND "
                                                              + StorageMediaContract.COLUMN_EXCURSION_UUID + " = ?")
                                               .whereArgs(sightUuid, excursionUuid)
                                               .build())
                            .prepare()
                            .executeAsBlocking()
            );
        });
    }

    @NonNull
    Single<PutResults<StorageMedia>> saveMedias(@NonNull List<StorageMedia> storageMedia) {
        return Single.fromCallable(() -> {
            return getSqLite().put()
                    .objects(storageMedia)
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<DeleteResult> deleteMedias(@NonNull String sightUuid, @NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return getSqLite().delete()
                    .byQuery(DeleteQuery.builder()
                                     .table(StorageMediaContract.TABLE_NAME)
                                     .where(StorageMediaContract.COLUMN_SIGHT_UUID + " = ? AND "
                                                    + StorageMediaContract.COLUMN_EXCURSION_UUID + " = ?")
                                     .whereArgs(sightUuid, excursionUuid)
                                     .build())
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<Optional<List<StoragePath>>> getPaths(@NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return Optional.Companion.of(
                    getSqLite().get()
                            .listOfObjects(StoragePath.class)
                            .withQuery(Query.builder()
                                               .table(StoragePathContract.TABLE_NAME)
                                               .where(StoragePathContract.COLUMN_EXCURSION_UUID + " = ?")
                                               .whereArgs(excursionUuid)
                                               .build())
                            .prepare()
                            .executeAsBlocking()
            );
        });
    }

    @NonNull
    Single<PutResult> savePath(@NonNull StoragePath storagePath) {
        return Single.fromCallable(() -> {
            return getSqLite().put()
                    .object(storagePath)
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<DeleteResult> deletePaths(@NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return getSqLite().delete()
                    .byQuery(DeleteQuery.builder()
                                     .table(StoragePathContract.TABLE_NAME)
                                     .where(StoragePathContract.COLUMN_EXCURSION_UUID + " = ?")
                                     .whereArgs(excursionUuid)
                                     .build())
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<Optional<List<StoragePoint>>> getPoints(@NonNull String pathUuid, @NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return Optional.Companion.of(
                    getSqLite().get()
                            .listOfObjects(StoragePoint.class)
                            .withQuery(Query.builder()
                                               .table(StoragePointContract.TABLE_NAME)
                                               .where(StoragePointContract.COLUMN_PATH_UUID + " = ? AND "
                                                              + StoragePointContract.COLUMN_EXCURSION_UUID + " = ?")
                                               .whereArgs(pathUuid, excursionUuid)
                                               .build())
                            .prepare()
                            .executeAsBlocking()
            );
        });
    }

    @NonNull
    Single<PutResults<StoragePoint>> savePoints(@NonNull List<StoragePoint> points) {
        return Single.fromCallable(() -> {
            return getSqLite().put()
                    .objects(points)
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<DeleteResult> deletePoints(@NonNull String pathUuid, @NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return getSqLite().delete()
                    .byQuery(DeleteQuery.builder()
                                     .table(StoragePointContract.TABLE_NAME)
                                     .where(StoragePointContract.COLUMN_PATH_UUID + " = ? AND "
                                                    + StoragePointContract.COLUMN_EXCURSION_UUID + " = ?")
                                     .whereArgs(pathUuid, excursionUuid)
                                     .build())
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<Optional<StorageBoughtExcursion>> getBoughtExcursion(@NonNull String excursionUuid) {
        return Single.fromCallable(() -> {
            return Optional.Companion.of(
                    getSqLite().get()
                            .object(StorageBoughtExcursion.class)
                            .withQuery(Query.builder()
                                               .table(StorageBoughtExcursionContract.TABLE_NAME)
                                               .where(StorageBoughtExcursionContract.COLUMN_UUID + " = ?")
                                               .whereArgs(excursionUuid)
                                               .build())
                            .prepare()
                            .executeAsBlocking()
            );
        });
    }

    @NonNull
    Single<Optional<PutResult>> saveBoughtExcursion(@NonNull StorageBoughtExcursion boughtExcursion) {
        return Single.fromCallable(() -> {
            return Optional.Companion.of(
                    getSqLite().put()
                            .object(boughtExcursion)
                            .prepare()
                            .executeAsBlocking()
            );
        });
    }

    @NonNull
    Single<Optional<List<StorageBoughtExcursion>>> getBoughtExcursions() {
        return Single.fromCallable(() -> {
            return Optional.Companion.of(
                    getSqLite().get()
                            .listOfObjects(StorageBoughtExcursion.class)
                            .withQuery(Query.builder()
                                               .table(StorageBoughtExcursionContract.TABLE_NAME)
                                               .build())
                            .prepare()
                            .executeAsBlocking()
            );
        });
    }

    @NonNull
    Single<PutResults<StorageBoughtExcursion>> saveBoughtExcursions(@NonNull List<StorageBoughtExcursion> excursions) {
        return Single.fromCallable(() -> {
            return getSqLite().put()
                    .objects(excursions)
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    Single<DeleteResult> deleteBoughtExcursions() {
        return Single.fromCallable(() -> {
            return getSqLite().delete()
                    .byQuery(DeleteQuery.builder()
                                     .table(StorageBoughtExcursionContract.TABLE_NAME)
                                     .build())
                    .prepare()
                    .executeAsBlocking();
        });
    }
}
