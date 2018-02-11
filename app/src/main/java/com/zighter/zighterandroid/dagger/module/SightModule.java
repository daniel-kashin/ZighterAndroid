package com.zighter.zighterandroid.dagger.module;

import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.dagger.scope.SightScope;
import com.zighter.zighterandroid.presentation.excursion.sight.SightPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_UI;
import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_WORKER;

@Module
public class SightModule {
    @Provides
    @SightScope
    SightPresenter.Builder provideSightPresenterBuilder(@Named(TAG_WORKER) Scheduler worker,
                                                        @Named(TAG_UI) Scheduler ui) {
        return new SightPresenter.Builder(worker, ui);
    }
}
