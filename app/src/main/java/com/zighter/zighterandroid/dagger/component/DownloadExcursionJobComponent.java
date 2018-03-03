package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.scope.DownloadExcursionJobScope;
import com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationBroadcastReceiver;
import com.zighter.zighterandroid.data.download_excursion.DownloadExcursionJob;

import dagger.Subcomponent;

@DownloadExcursionJobScope
@Subcomponent()
public interface DownloadExcursionJobComponent {
    void inject(DownloadExcursionJob downloadExcursionJob);

    void inject(DownloadExcursionNotificationBroadcastReceiver downloadExcursionNotificationBroadcastReceiver);
}
