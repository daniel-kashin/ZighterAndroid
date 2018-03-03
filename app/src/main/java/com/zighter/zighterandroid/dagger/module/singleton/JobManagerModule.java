package com.zighter.zighterandroid.dagger.module.singleton;

import android.content.Context;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.zighter.zighterandroid.dagger.JobDependencyInjector;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class JobManagerModule {
    @Singleton
    @Provides
    JobManager provideJobManager(Context context) {
        Configuration configuration = new Configuration.Builder(context)
                .injector(new JobDependencyInjector())
                .build();

        JobManager jobManager = new JobManager(configuration);
        jobManager.start();

        return jobManager;
    }
}
