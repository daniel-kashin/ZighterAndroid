package com.zighter.zighterandroid.data.repositories.paths;

import com.zighter.zighterandroid.data.ZighterEndpoints;
import com.zighter.zighterandroid.data.common.BaseService;
import com.zighter.zighterandroid.data.entities.service.Excursion;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class PathsService extends BaseService<PathsContract> {

    public PathsService(OkHttpClient okHttpClient) {
        super(ZighterEndpoints.BASE_URL, okHttpClient);
    }

    @Override
    protected PathsContract createService(Retrofit retrofit) {
        return retrofit.create(PathsContract.class);
    }

    Single<Excursion> getExcursion() {
        return getService().getExcursion();
    }

}
