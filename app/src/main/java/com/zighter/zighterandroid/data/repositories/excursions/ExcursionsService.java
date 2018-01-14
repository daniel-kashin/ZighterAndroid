package com.zighter.zighterandroid.data.repositories.excursions;

import com.google.gson.Gson;
import com.zighter.zighterandroid.data.ZighterEndpoints;
import com.zighter.zighterandroid.data.common.BaseService;
import com.zighter.zighterandroid.data.entities.service.Excursion;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ExcursionsService extends BaseService<ExcursionsContract> {

    private static final String HARDCODED = "{\"pub_status\": 0, \"name\": \"\u041c\u043e\u0441\u043a\u0432\u0430\", \"route\": {\"paths\": [{\"endpoint\": [{\"lng\": 37.52998352050782, \"lat\": 55.748806143437804}, {\"lng\": 37.53264427185059, \"lat\": 55.754844102726736}], \"type\": \"1\", \"endpointId\": [5, 6], \"id\": \"4\", \"point\": [{\"lng\": 37.52998352050782, \"lat\": 55.748806143437804}, {\"lng\": 37.54749298095704, \"lat\": 55.75402299517174}, {\"lng\": 37.54920959472657, \"lat\": 55.75600328396566}, {\"lng\": 37.54646301269532, \"lat\": 55.75754880536109}, {\"lng\": 37.54096984863282, \"lat\": 55.75658286166609}, {\"lng\": 37.539854049682624, \"lat\": 55.76175038205827}, {\"lng\": 37.53264427185059, \"lat\": 55.754844102726736}]}], \"sights\": [{\"name\": null, \"id\": \"0\", \"point\": {\"lng\": 37.52655029296876, \"lat\": 55.74861291330844}, \"type\": \"1\"}, {\"name\": null, \"id\": \"1\", \"point\": {\"lng\": 37.55538940429688, \"lat\": 55.755133901266255}, \"type\": \"1\"}, {\"name\": null, \"id\": \"2\", \"point\": {\"lng\": 37.53418922424317, \"lat\": 55.75967379727972}, \"type\": \"1\"}, {\"name\": null, \"id\": \"3\", \"point\": {\"lng\": 37.55101203918458, \"lat\": 55.76088112746782}, \"type\": \"1\"}]}, \"user\": 2, \"last_update\": \"2017-11-26T16:12:18Z\", \"timestamp\": \"2017-11-26T16:12:18Z\", \"id\": 90}";

    public ExcursionsService(OkHttpClient okHttpClient) {
        super(ZighterEndpoints.BASE_URL, okHttpClient);
    }

    @Override
    protected ExcursionsContract createService(Retrofit retrofit) {
        return retrofit.create(ExcursionsContract.class);
    }

    Single<Excursion> getExcursion() {
        //return getService().getExcursion();
        return Single.fromCallable(() -> {
            Gson gson = new Gson();
            return gson.fromJson(HARDCODED, Excursion.class);
        });
    }

}
