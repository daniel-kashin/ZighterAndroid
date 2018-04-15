package com.zighter.zighterandroid.data.network;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.service.ServiceExcursionBindResponse;
import com.zighter.zighterandroid.data.entities.service.ServiceLoginData;
import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceGuide;
import com.zighter.zighterandroid.data.entities.service.ServiceRegistrationData;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchExcursions;
import com.zighter.zighterandroid.data.entities.service.ServiceToken;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.API;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.BIND;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.CLIENT_COLLECTION;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.EXCURSIONS;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.GUIDES;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.REGISTER;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.ROUTE;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.ROUTES;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.SLASH;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.TOKEN_AUTH;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.V1;

public interface ServiceExcursionContract {
    @GET(API + V1 + ROUTES + "/{uuid}")
    Single<ServiceExcursion> getExcursion(@NonNull @Path("uuid") String uuid,
                                          @NonNull @Header("Authorization") String token);

    @GET(API + V1 + GUIDES + "/{uuid}")
    Single<ServiceGuide> getGuide(@NonNull @Path("uuid") String ownerUuid,
                                  @NonNull @Header("Authorization") String token);


    @GET(API + V1 + CLIENT_COLLECTION)
    Single<List<ServiceBoughtExcursion>> getBoughtExcursions(@NonNull @Header("Authorization") String token);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST(API + V1 + TOKEN_AUTH + SLASH)
    Single<ServiceToken> login(@NonNull @Body ServiceLoginData data);


    @POST(API + V1 + REGISTER + SLASH)
    Single<ServiceToken> register(@NonNull @Body ServiceRegistrationData data);

    @GET(API + V1 + EXCURSIONS + SLASH)
    Single<ServiceSearchExcursions> searchExcursions(@NonNull @Query("q") String request,
                                                     @Query("location") boolean isLocation,
                                                     @Query("itemNum") int excursionCount);

    @GET(API + V1 + EXCURSIONS + SLASH)
    Single<ServiceSearchExcursions> searchExcursionsWithSort(@NonNull @Query("q") String request,
                                                             @Query("location") boolean isLocation,
                                                             @Query("hasSort") boolean hasSort,
                                                             @Query("sortType") String sortType,
                                                             @Query("sortOrder") String sortOrder,
                                                             @Query("itemNum") int excursionCount);

    @POST(API + V1 + BIND + "/{uuid}" + ROUTE + SLASH)
    Single<ServiceExcursionBindResponse> bindExcursionRoute(@NonNull @Path("uuid") String excursionUuid,
                                                            @NonNull @Header("Authorization") String token);
}
