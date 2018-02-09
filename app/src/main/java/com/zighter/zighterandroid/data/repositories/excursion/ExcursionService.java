package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.ZighterEndpoints;
import com.zighter.zighterandroid.data.common.BaseService;
import com.zighter.zighterandroid.data.entities.excursion.ServiceExcursion;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;

import java.io.IOException;

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

    Single<ServiceExcursion> getExcursion() {
        return getService().getExcursion()
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    private SingleSource<? extends ServiceExcursion> convertRetrofitThrowable(Throwable error) {
        if (error instanceof HttpException) {
            return Single.error(new ServerException(error));
        }

        if (error instanceof IOException) {
            return Single.error(new NetworkUnavailableException(error));
        }

        return Single.error(error);
    }
}
