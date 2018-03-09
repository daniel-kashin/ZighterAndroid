package com.zighter.zighterandroid.dagger.module;

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
    SightPresenter.Builder provideSightPresenterBuilder(@Named(TAG_WORKER) Scheduler worker,
                                                        @Named(TAG_UI) Scheduler ui) {
        return new SightPresenter.Builder(worker, ui);
    }
}
