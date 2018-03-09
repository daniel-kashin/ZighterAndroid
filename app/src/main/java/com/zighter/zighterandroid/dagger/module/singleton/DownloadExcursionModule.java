package com.zighter.zighterandroid.dagger.module.singleton;

import android.content.Context;

import com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DownloadExcursionModule {
    @Singleton
    @Provides
    DownloadExcursionNotificationManager provideNotificationManager(Context context) {
        return new DownloadExcursionNotificationManager(context);
    }
}
