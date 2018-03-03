package com.zighter.zighterandroid.data.download_excursion;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.repositories.excursion.DownloadStatus;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;

import java.io.Serializable;
import java.util.concurrent.CancellationException;

import javax.inject.Inject;

import io.reactivex.Observable;

public class DownloadExcursionJob extends Job implements Serializable {
    private static final String TAG = "DownloadExcursionJob";

    private static final int PRIORITY = 10;

    @Inject
    @Nullable
    ExcursionRepository excursionRepository;
    @Inject
    @Nullable
    DownloadExcursionNotificationManager notificationManager;
    @Inject
    @Nullable
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
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(TAG, "onRun");
        if (excursionRepository == null || notificationManager == null || applicationContext == null) {
            return;
        }

        notificationManager.updateNotification(boughtExcursion, null);

        for (int i = 0; i < 10; ++i) {
            if (isCancelled()) {
                throw new CancellationException();
            }
            notificationManager.updateNotification(boughtExcursion, new DownloadStatus(DownloadStatus.Type.DATABASE, i, 10));
            Thread.sleep(2000);
        }

        notificationManager.cancelNotification();

        DownloadExcursionProgressContract.notifySuccess(applicationContext, boughtExcursion);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.d(TAG, "onCancel");
        if (excursionRepository == null || notificationManager == null || applicationContext == null) {
            return;
        }

        notificationManager.cancelNotification();

        DownloadExcursionProgressContract.notifyException(applicationContext, boughtExcursion);
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        Log.d(TAG, "shouldReRunOnThrowable");
        return null;
    }
}
