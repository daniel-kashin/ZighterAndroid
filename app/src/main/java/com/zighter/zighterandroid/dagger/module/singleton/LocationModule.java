package com.zighter.zighterandroid.dagger.module.singleton;

import android.location.LocationManager;

import com.zighter.zighterandroid.data.location.LocationListenerHolder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocationModule {
    @Singleton
    @Provides
    LocationListenerHolder provideLocationListenerHolder(LocationManager locationManager) {
        return new LocationListenerHolder(locationManager);
    }
}
