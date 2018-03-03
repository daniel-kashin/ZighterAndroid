package com.zighter.zighterandroid.data.repositories.common;

import android.support.annotation.NonNull;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseService<S> {

    private S dataService;

    public BaseService(@NonNull String baseUrl, @NonNull OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        this.dataService = createService(retrofit);
    }

    protected S getService() {
        return this.dataService;
    }

    protected abstract S createService(Retrofit retrofit);

}
