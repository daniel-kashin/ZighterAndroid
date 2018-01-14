package com.zighter.zighterandroid.data.repositories.excursions;

import com.zighter.zighterandroid.data.entities.service.Excursion;

import io.reactivex.Single;
import retrofit2.http.GET;

import static com.zighter.zighterandroid.data.ZighterEndpoints.JSON_LIST;
import static com.zighter.zighterandroid.data.ZighterEndpoints.ROUTE;

interface ExcursionsContract {

    @GET(ROUTE + JSON_LIST + "/90")
    Single<Excursion> getExcursion();

}
