package com.zighter.zighterandroid.data.download_excursion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract;
import com.zighter.zighterandroid.data.repositories.excursion.DownloadProgress;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;

import java.io.Serializable;

import javax.inject.Inject;

public class DownloadExcursionJob extends Job implements Serializable {
    private static final String TAG = "DownloadExcursionJob";

    private static final int PRIORITY = 10;

    @Inject
    ExcursionRepository excursionRepository;
    @Inject
    DownloadExcursionNotificationManager notificationManager;
    @Inject
    Context applicationContext;

    @NonNull
    private final BoughtExcursion boughtExcursion;

    public DownloadExcursionJob(@NonNull BoughtExcursion boughtExcursion) {
        super(new Params(PRIORITY)
                      .singleInstanceBy(boughtExcursion.getUuid())
                      .addTags(boughtExcursion.getUuid())
                      .requireNetwork()
                      .persist());

        this.boughtExcursion = boughtExcursion;
    }

    @Override
    public void onAdded() {
        Log.d(TAG, "onAdded");
        JobManagerProgressContract.notifyAdded(applicationContext, boughtExcursion);
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(TAG, "onRun");

        JobManagerProgressContract.notifyStarted(applicationContext, boughtExcursion);

        notificationManager.updateNotification(boughtExcursion, null);

        for (int i = 0; i < 10; ++i) {
            if (isCancelled()) {
                return;
            } else {
                notificationManager.updateNotification(boughtExcursion, new DownloadProgress(DownloadProgress.Type.DATABASE, i, 10));
                Thread.sleep(2000);
            }
        }

        JobManagerProgressContract.notifySuccess(applicationContext, boughtExcursion);

        notificationManager.cancelNotification();
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.d(TAG, "onCancel");

        if (cancelReason != CancelReason.CANCELLED_WHILE_RUNNING) {
            JobManagerProgressContract.notifyException(applicationContext, boughtExcursion);

            notificationManager.cancelNotification();
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        Log.d(TAG, "shouldReRunOnThrowable");
        return RetryConstraint.CANCEL;
    }
}
