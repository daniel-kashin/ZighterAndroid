package com.zighter.zighterandroid.dagger.module;

import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.guide.GuidePresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_UI;
import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_WORKER;

import static com.zighter.zighterandroid.presentation.guide.GuidePresenter.GuidePresenterBuilder;

@Module
public class GuideModule {
    @Provides
    GuidePresenterBuilder provideGuidePresenterBuilder(@Named(TAG_WORKER) Scheduler worker,
                                                       @Named(TAG_UI) Scheduler ui,
                                                       ExcursionRepository excursionRepository) {
        return new GuidePresenterBuilder(worker, ui, excursionRepository);
    }
}
