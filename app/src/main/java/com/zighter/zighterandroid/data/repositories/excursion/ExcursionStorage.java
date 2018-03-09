package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio3.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio3.sqlite.queries.Query;
import com.zighter.zighterandroid.data.database.StorageBoughtExcursionContract;
import com.zighter.zighterandroid.data.entities.storage.StorageBoughtExcursion;
import com.zighter.zighterandroid.data.repositories.common.BaseStorage;
import com.zighter.zighterandroid.util.Optional;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.DELETE;

public class ExcursionStorage extends BaseStorage {

    public ExcursionStorage(@NonNull StorIOSQLite storIOSQLite) {
        super(storIOSQLite);
    }

    @NonNull
    public Single<Optional<List<StorageBoughtExcursion>>> getBoughtExcursions() {
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
    public Single<PutResults<StorageBoughtExcursion>> saveBoughtExcursions(@NonNull List<StorageBoughtExcursion> excursions) {
        return Single.fromCallable(() -> {
            return getSqLite().put()
                    .objects(excursions)
                    .prepare()
                    .executeAsBlocking();
        });
    }

    @NonNull
    public Single<DeleteResult> deleteBoughtExcursions() {
        return Single.fromCallable(() -> {
            return getSqLite().delete()
                    .byQuery(DeleteQuery
                                     .builder()
                                     .table(StorageBoughtExcursionContract.TABLE_NAME)
                                     .build())
                    .prepare()
                    .executeAsBlocking();
        });
    }
}
