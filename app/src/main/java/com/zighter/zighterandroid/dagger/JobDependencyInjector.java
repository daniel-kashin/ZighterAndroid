package com.zighter.zighterandroid.dagger;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.di.DependencyInjector;
import com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionJob;

public class JobDependencyInjector implements DependencyInjector {
    @Override
    public void inject(Job job) {
        if (job == null) {
            return;
        }

        if (job instanceof DownloadExcursionJob) {
            Injector.getInstance()
                    .getDowndloadExcursionJobComponent()
                    .inject((DownloadExcursionJob) job);
        }
    }
}
