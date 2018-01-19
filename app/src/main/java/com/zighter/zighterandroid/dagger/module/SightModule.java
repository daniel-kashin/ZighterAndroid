package com.zighter.zighterandroid.dagger.module;

import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.dagger.scope.SightScope;
import com.zighter.zighterandroid.presentation.excursion.sight.SightPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SightModule {
    @Provides
    @SightScope
    SightPresenter.Builder provideSightPresenterBuilder() {
        return new SightPresenter.Builder();
    }
}
