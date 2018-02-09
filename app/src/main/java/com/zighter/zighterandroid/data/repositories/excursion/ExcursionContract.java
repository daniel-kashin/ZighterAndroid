package com.zighter.zighterandroid.data.repositories.excursion;

import com.zighter.zighterandroid.data.entities.excursion.ServiceExcursion;

import io.reactivex.Single;
import retrofit2.http.GET;

import static com.zighter.zighterandroid.data.ZighterEndpoints.JSON_LIST;
import static com.zighter.zighterandroid.data.ZighterEndpoints.ROUTE;

interface ExcursionContract {
    @GET(ROUTE + JSON_LIST + "/8")
    Single<ServiceExcursion> getExcursion();
}
