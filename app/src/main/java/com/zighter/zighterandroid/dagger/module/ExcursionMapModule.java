package com.zighter.zighterandroid.dagger.module;

import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.excursion.map.ExcursionMapPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_WORKER;
import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_UI;

@Module
public class ExcursionMapModule {
    @Provides
    ExcursionMapPresenter.Builder provideNavigationPresenter(ExcursionRepository excursionRepository,
                                                             @Named(TAG_WORKER) Scheduler worker,
                                                             @Named(TAG_UI) Scheduler ui) {
        return new ExcursionMapPresenter.Builder(excursionRepository, worker, ui);
    }
}
