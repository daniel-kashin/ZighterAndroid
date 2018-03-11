package com.zighter.zighterandroid.data.repositories.excursion;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.service.ServiceBoughtExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceExcursion;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.CLIENT_EXCURSIONS;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.HOME;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.JSON_LIST;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.JSON_LIST_UNDERLINE;
import static com.zighter.zighterandroid.data.repositories.ZighterEndpoints.ROUTE;

interface ServiceExcursionContract {
    @GET(ROUTE + JSON_LIST_UNDERLINE + "/{uuid}")
    Single<ServiceExcursion> getExcursion(@NonNull @Path("uuid") String uuid);

    @GET(HOME + CLIENT_EXCURSIONS + "/1" + JSON_LIST)
    Single<List<ServiceBoughtExcursion>> getBoughtExcursions();

    @GET("{url}")
    Single<ResponseBody> downloadFile(@NonNull @Path("url") String url);
}
