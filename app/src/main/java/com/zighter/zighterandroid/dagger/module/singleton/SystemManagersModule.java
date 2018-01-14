package com.zighter.zighterandroid.dagger.module.singleton;

import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.LOCATION_SERVICE;

@Module
public class SystemManagersModule {

    @Singleton
    @Provides
    LocationManager provideLocationManager(@NonNull Context context) {
        return (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }


}
