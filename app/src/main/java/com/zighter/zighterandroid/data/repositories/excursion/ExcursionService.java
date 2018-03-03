package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.ZighterEndpoints;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.repositories.common.BaseService;
import com.zighter.zighterandroid.data.entities.excursion.ServiceExcursion;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import okhttp3.OkHttpClient;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class ExcursionService extends BaseService<ExcursionContract> {
    public ExcursionService(OkHttpClient okHttpClient) {
        super(ZighterEndpoints.BASE_URL, okHttpClient);
    }

    @Override
    protected ExcursionContract createService(Retrofit retrofit) {
        return retrofit.create(ExcursionContract.class);
    }

    @NonNull
    Single<ServiceExcursion> getExcursion() {
        return getService().getExcursion()
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    Single<List<BoughtExcursion>> getBoughtExcursions() {
        return getService().getBoughtExcursions()
                .onErrorResumeNext(this::convertRetrofitThrowable);
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
