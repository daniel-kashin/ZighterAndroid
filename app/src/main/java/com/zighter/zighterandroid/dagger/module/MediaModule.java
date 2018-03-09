package com.zighter.zighterandroid.dagger.module;

import com.zighter.zighterandroid.presentation.media.MediaPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_WORKER;
import static com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule.TAG_UI;

@Module
public class MediaModule {
    @Provides
    MediaPresenter.Builder provideMediaPresenterBuilder(@Named(TAG_WORKER) Scheduler worker,
                                                        @Named(TAG_UI) Scheduler ui) {
        return new MediaPresenter.Builder(worker, ui);
    }
}
