package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.gson.annotations.SerializedName;
import com.zighter.zighterandroid.data.entities.service.ServiceLoginData;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceGuide;
import com.zighter.zighterandroid.data.entities.service.ServiceRegistrationData;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchExcursions;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchSort;
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

import static com.zighter.zighterandroid.data.entities.service.ServiceSearchSort.SortOrder.ASC;
import static com.zighter.zighterandroid.data.entities.service.ServiceSearchSort.SortType.DATE;

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
        return getService()
                .login(new ServiceLoginData(username, password))
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    Single<ServiceToken> register(@NonNull String email,
                                  @NonNull String firstName,
                                  @NonNull String lastName,
                                  @NonNull String password,
                                  @NonNull String username) {
        return getService()
                .register(new ServiceRegistrationData(email, firstName, lastName, password, username))
                .onErrorResumeNext(this::convertRetrofitThrowable)
                .doOnSuccess(serviceToken -> {
                    if (!serviceToken.isValid()) {
                        throw new ServerLoginException(null);
                    }
                });
    }

    @NonNull
    Single<ServiceSearchExcursions> searchExcursions(@NonNull String request,
                                                     @NonNull ServiceSearchSort sort) {
        if (sort.getSortOrder() == null || sort.getSortType() == null) {
            return getService()
                    .searchExcursions(request, false)
                    .onErrorResumeNext(this::convertRetrofitThrowable);
        } else {
            return getService()
                    .searchExcursionsWithSort(request,
                                              false,
                                              true,
                                              sort.getSortType() == DATE ? "dataTime" : "routePrice",
                                              sort.getSortOrder() == ASC ? "asc" : "desc")
                    .onErrorResumeNext(this::convertRetrofitThrowable);
        }
    }

    @NonNull
    Single<ServiceGuide> getGuide(@NonNull String ownerUuid, @NonNull String token) {
        return getService()
                .getGuide(ownerUuid, addTokenPrefix(token))
                .onErrorResumeNext(this::convertRetrofitThrowable);
    }

    @NonNull
    Single<ServiceExcursion> getExcursion(@NonNull String uuid, @NonNull String token) {
        return getService()
                .getExcursion(uuid, addTokenPrefix(token))
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
        return getService()
                .getBoughtExcursions(addTokenPrefix(token))
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
    private String addTokenPrefix(@NonNull String token) {
        return TOKEN_PREFIX + token;
    }
}
