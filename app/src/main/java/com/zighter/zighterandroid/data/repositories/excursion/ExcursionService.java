package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.zighter.zighterandroid.data.entities.service.ServiceAuthorizationData;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceGuide;
import com.zighter.zighterandroid.data.entities.service.ServiceToken;
import com.zighter.zighterandroid.data.exception.ServerLoginException;
import com.zighter.zighterandroid.data.exception.ServerNoAccessException;
import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;
import com.zighter.zighterandroid.data.network.ServiceExcursionContract;
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
    private static final String TOKEN_PREFIX = "Token ";

    public ExcursionService(OkHttpClient okHttpClient) {
        super(ZighterEndpoints.BASE_URL, okHttpClient);
    }

    @Override
    protected ServiceExcursionContract createService(Retrofit retrofit) {
        return retrofit.create(ServiceExcursionContract.class);
    }

    @NonNull
    Single<ServiceToken> login(@NonNull String username, @NonNull String password) {
        return getService().login(new ServiceAuthorizationData(username, password))
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    Single<ServiceGuide> getGuide(@NonNull String uuid, @NonNull String token) {
        return getService().getGuide(uuid, addTokenPrefix(token))
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    Single<ServiceExcursion> getExcursion(@NonNull String uuid, @NonNull String token) {
        return getService().getExcursion(uuid, addTokenPrefix(token))
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    Single<Pair<List<ServiceBoughtExcursion>, Exception>> getBoughtExcursionsOrException(@NonNull String token) {
        return getBoughtExcursions(token)
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
    private Single<List<ServiceBoughtExcursion>> getBoughtExcursions(@NonNull String token) {
        return getService().getBoughtExcursions(addTokenPrefix(token))
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    private <T> SingleSource<T> convertRetrofitThrowable(@NonNull Throwable error) {
        if (error instanceof HttpException) {
            Throwable result;

            int code = ((HttpException) error).code();
            if (code == 400) {
                result = new ServerLoginException(error);
            } else  if (code == 401) {
                result = new ServerNotAuthorizedException(error);
            } else if (code == 403) {
                result = new ServerNoAccessException(error);
            } else {
                result = new ServerException(error);
            }

            return Single.error(result);
        }

        if (error instanceof IOException) {
            return Single.error(new NetworkUnavailableException(error));
        }

        return Single.error(error);
    }

    @NonNull
    private Throwable convertRetrofitThrowableInner(@NonNull Throwable error) {
        Throwable result = error;

        if (error instanceof HttpException) {
            int code = ((HttpException) error).code();
            if (code == 400) {
                result = new ServerLoginException(error);
            } else  if (code == 401) {
                result = new ServerNotAuthorizedException(error);
            } else if (code == 403) {
                result = new ServerNoAccessException(error);
            } else {
                result = new ServerException(error);
            }

        }

        if (error instanceof IOException) {
            result = new NetworkUnavailableException(error);
        }

        return result;
    }

    @NonNull
    private String addTokenPrefix(@NonNull String token) {
        return TOKEN_PREFIX + token;
    }
}
