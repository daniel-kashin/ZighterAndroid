package com.zighter.zighterandroid.dagger.module.singleton;

import android.content.Context;
import android.support.annotation.NonNull;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.zighter.zighterandroid.data.job_manager.JobManagerWrapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    @NonNull
    private final Context context;
    @NonNull
    private final JobManagerWrapper jobManagerWrapper;

    public AppModule(@NonNull Context context) {
        this.context = context.getApplicationContext();

        Configuration configuration = new Configuration.Builder(context)
                .build();

        jobManagerWrapper = new JobManagerWrapper(new JobManager(configuration));
        jobManagerWrapper.start(context);
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }

    @Provides
    @Singleton
    JobManagerWrapper provideJobManagerWrapper() {
        return jobManagerWrapper;
    }
}
