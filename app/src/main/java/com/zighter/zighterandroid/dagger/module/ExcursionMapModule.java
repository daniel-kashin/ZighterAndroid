package com.zighter.zighterandroid.dagger.module;

import com.zighter.zighterandroid.dagger.scope.NavigationScope;
import com.zighter.zighterandroid.data.repositories.excursions.ExcursionsRepository;
import com.zighter.zighterandroid.presentation.excursion.map.ExcursionMapPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_IO;
import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_UI;

@Module
public class ExcursionMapModule {

    @NavigationScope
    @Provides
    ExcursionMapPresenter provideNavigationPresenter(ExcursionsRepository excursionsRepository,
                                                     @Named(TAG_IO) Scheduler worker,
                                                     @Named(TAG_UI) Scheduler ui) {
        return new ExcursionMapPresenter(excursionsRepository, worker, ui);
    }

}
