package com.zighter.zighterandroid.dagger.module;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.registration.RegistrationPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_UI;
import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_WORKER;

@Module
public class RegistrationModule {
    @Provides
    public RegistrationPresenter provideRegistrationPresenter(ExcursionRepository excursionRepository,
                                                              @Named(TAG_WORKER) Scheduler worker,
                                                              @Named(TAG_UI) Scheduler ui) {
        return new RegistrationPresenter(worker, ui, excursionRepository);
    }
}
