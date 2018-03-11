package com.zighter.zighterandroid.data.job_manager.download_excursion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.birbit.android.jobqueue.TagConstraint;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.job_manager.JobManagerEventContract;
import com.zighter.zighterandroid.data.job_manager.JobManagerWrapper;

import javax.inject.Inject;

import static com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationContract.ACTION_CANCEL_JOB;
import static com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationContract.EXTRA_EXCURSION;

public class DownloadExcursionNotificationBroadcastReceiver extends BroadcastReceiver {
    @Inject
    JobManagerWrapper jobManagerWrapper;
    @Inject
    DownloadExcursionNotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationBR", "onReceive(" + (intent == null ? "null" : intent.getAction()) + ")");
        if (context == null || intent == null || intent.getAction() == null) {
            return;
        }

        switch (intent.getAction()) {
            case ACTION_CANCEL_JOB: {
                BoughtExcursion boughtExcursion = (BoughtExcursion) intent.getSerializableExtra(EXTRA_EXCURSION);
                if (boughtExcursion == null) return;

                Injector injector = Injector.getInstance();
                injector.getDowndloadExcursionJobComponent().inject(this);

                notificationManager.cancelNotification(boughtExcursion);
                JobManagerEventContract.notifyCancelled(context, boughtExcursion.getUuid());
                jobManagerWrapper.getJobManager().cancelJobsInBackground(null, TagConstraint.ANY, boughtExcursion.getUuid());

                break;
            }
        }
    }
}
