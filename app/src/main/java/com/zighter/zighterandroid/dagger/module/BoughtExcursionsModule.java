package com.zighter.zighterandroid.dagger.module;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.zighter.zighterandroid.dagger.scope.BoughtExcursionsScope;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.bought_excursions.BoughtExcursionsPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_UI;
import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_WORKER;

@Module
public class BoughtExcursionsModule {
    @Provides
    @BoughtExcursionsScope
    BoughtExcursionsPresenter provideBoughtExcursionsPresenter(Context context,
                                                               ExcursionRepository excursionRepository,
                                                               @Named(TAG_WORKER) Scheduler worker,
                                                               @Named(TAG_UI) Scheduler ui) {
        return new BoughtExcursionsPresenter(context, excursionRepository, worker, ui);
    }
}
