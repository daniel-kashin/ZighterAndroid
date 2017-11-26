package com.zighter.zighterandroid.dagger.module.singleton;

import android.support.v4.content.PermissionChecker;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class SchedulerModule {

    public static final String TAG_IO = "IO";

    public static final String TAG_UI = "UI";

    @Provides
    @Named(TAG_IO)
    public Scheduler getWorkerScheduler() {
        return Schedulers.io();
    }

    @Provides
    @Named(TAG_UI)
    public Scheduler getMainScheduler() {
        return AndroidSchedulers.mainThread();
    }

}
