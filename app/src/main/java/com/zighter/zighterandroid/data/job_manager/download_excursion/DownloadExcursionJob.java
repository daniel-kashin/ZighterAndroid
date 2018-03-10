package com.zighter.zighterandroid.data.job_manager.download_excursion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.job_manager.JobManagerEventContract;
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
                      .groupBy(TAG)
                      .requireNetwork()
                      .persist());

        this.boughtExcursion = boughtExcursion;
    }

    @Override
    public void onAdded() {
        Log.d(TAG, "onAdded");
        JobManagerEventContract.notifyAdded(applicationContext, getSingleIdWithoutPrefix());
    }

    @NonNull
    private String getSingleIdWithoutPrefix() {
        String id = boughtExcursion.getUuid();
        Log.d(TAG,"getSingleIdWithoutPrefix(" + id + ")");
        return id;
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(TAG, "onRun");

        JobManagerEventContract.notifyStarted(applicationContext, getSingleIdWithoutPrefix());

        notificationManager.updateNotification(boughtExcursion, null);

        for (int i = 0; i < 10; ++i) {
            if (isCancelled()) {
                notificationManager.cancelNotification(boughtExcursion);
                return;
            } else {
                notificationManager.updateNotification(boughtExcursion, new DownloadProgress(DownloadProgress.Type.DATABASE, i, 10));
                Thread.sleep(2000);
            }
        }

        notificationManager.cancelNotification(boughtExcursion);

        JobManagerEventContract.notifySuccess(applicationContext, getSingleIdWithoutPrefix());
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.d(TAG, "onCancel");

        switch (cancelReason) {
            case CancelReason.CANCELLED_VIA_SHOULD_RE_RUN:
                notificationManager.cancelNotification(boughtExcursion);
                JobManagerEventContract.notifyException(applicationContext, getSingleIdWithoutPrefix());
                break;
            case CancelReason.SINGLE_INSTANCE_ID_QUEUED:
            case CancelReason.SINGLE_INSTANCE_WHILE_RUNNING:
                int i = 0;
                // do nothing as another instance of this job exists in memory
                break;
            case CancelReason.CANCELLED_WHILE_RUNNING:
                notificationManager.cancelNotification(boughtExcursion);
                break;
            case CancelReason.REACHED_DEADLINE:
            case CancelReason.REACHED_RETRY_LIMIT:
                // not used
                break;
        }
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        Log.d(TAG, "shouldReRunOnThrowable");
        return RetryConstraint.CANCEL;
    }
}
