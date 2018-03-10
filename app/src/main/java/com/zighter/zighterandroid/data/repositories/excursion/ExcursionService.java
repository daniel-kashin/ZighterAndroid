package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.repositories.ZighterEndpoints;
import com.zighter.zighterandroid.data.repositories.common.BaseService;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursion;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class ExcursionService extends BaseService<ServiceExcursionContract> {
    public ExcursionService(OkHttpClient okHttpClient) {
        super(ZighterEndpoints.BASE_URL, okHttpClient);
    }

    @Override
    protected ServiceExcursionContract createService(Retrofit retrofit) {
        return retrofit.create(ServiceExcursionContract.class);
    }

    @NonNull
    Single<ServiceExcursion> getExcursion(@NonNull String uuid) {
        return getService().getExcursion(uuid)
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    Single<List<ServiceBoughtExcursion>> getBoughtExcursions() {
        return getService().getBoughtExcursions()
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    Single<Pair<List<ServiceBoughtExcursion>, Exception>> getBoughtExcursionsOrException() {
        return getBoughtExcursions()
                .map(it -> new Pair<List<ServiceBoughtExcursion>, Exception>(it, null))
                .onErrorResumeNext(throwable -> {
                    if (throwable instanceof Exception) {
                        return Single.just(new Pair<>(null, (Exception) throwable));
                    } else {
                        return Single.error(throwable);
                    }
                });
    }

    @NonNull
    private <T> SingleSource<T> convertRetrofitThrowable(Throwable error) {
        if (error instanceof HttpException) {
            return Single.error(new ServerException(error));
        }
        if (error instanceof IOException) {
            return Single.error(new NetworkUnavailableException(error));
        }
        return Single.error(error);
    }
}
