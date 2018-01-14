package com.zighter.zighterandroid.data.repositories.excursions;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.service.Excursion;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import retrofit2.HttpException;

public class ExcursionsRepository {

    @NonNull
    private final ExcursionsStorage excursionsStorage;
    @NonNull
    private final ExcursionsService excursionsService;

    public ExcursionsRepository(@NonNull ExcursionsService excursionsService, @NonNull ExcursionsStorage excursionsStorage) {
        this.excursionsService = excursionsService;
        this.excursionsStorage = excursionsStorage;
    }

    public Single<Excursion> getExcursion() {
        return excursionsService.getExcursion()
                .map(this::tryGetValidCopyOrThrowException)
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    private Excursion tryGetValidCopyOrThrowException(Excursion excursion) throws ServerResponseValidationException {
        if (excursion.isValid()) {
            return excursion;
        } else {
            Excursion copy = excursion.tryGetValidCopy();
            if (copy != null) {
                return copy;
            } else {
                throw new ServerResponseValidationException();
            }
        }
    }

    private SingleSource<? extends Excursion> convertRetrofitThrowable(Throwable error) {
        if (error instanceof HttpException) {
            return Single.error(new ServerException(error));
        }
        if (error instanceof IOException) {
            return Single.error(new NetworkUnavailableException(error));
        }
        return Single.error(error);
    }


}
