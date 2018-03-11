package com.zighter.zighterandroid.data.job_manager.download_excursion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.job_manager.JobManagerEventContract;
import com.zighter.zighterandroid.data.repositories.excursion.DownloadProgress;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.util.Optional;

import java.io.Serializable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DownloadExcursionJob extends Job implements Serializable {
    private static final String TAG = "DownloadExcursionJob";

    private static final int PRIORITY = 10;

    @Inject
    ExcursionRepository excursionRepository = null;
    @Inject
    DownloadExcursionNotificationManager notificationManager = null;
    @Inject
    Context applicationContext = null;

    @NonNull
    private final BoughtExcursion boughtExcursion;

    public DownloadExcursionJob(@NonNull BoughtExcursion boughtExcursion) {
        super(new Params(PRIORITY)
                      .singleInstanceBy(boughtExcursion.getUuid())
                      .addTags(boughtExcursion.getUuid(), TAG)
                      .groupBy(TAG)
                      .requireNetwork()
                      .persist());

        this.boughtExcursion = boughtExcursion;
    }

    @Override
    public void onAdded() {
        Log.d(TAG, "onAdded");
        injectDependenciesIfNotInjected();

        JobManagerEventContract.notifyAdded(applicationContext, getSingleIdWithoutPrefix());
    }

    @NonNull
    private String getSingleIdWithoutPrefix() {
        String id = boughtExcursion.getUuid();
        Log.d(TAG, "getSingleIdWithoutPrefix(" + id + ")");
        return id;
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(TAG, "onRun");
        injectDependenciesIfNotInjected();

        JobManagerEventContract.notifyStarted(applicationContext, getSingleIdWithoutPrefix());
        notificationManager.updateNotification(boughtExcursion, null);

        excursionRepository.downloadExcursion(boughtExcursion.getUuid())
                .sample(1500, TimeUnit.MILLISECONDS)
                .blockingSubscribe(new Consumer<DownloadProgress>() {
                    private final Object lock = new Object();
                    private volatile DownloadProgress latest = null;

                    @Override
                    public void accept(DownloadProgress downloadProgress) throws Exception {
                        if (isCancelled()) {
                            Log.d(TAG, "Observer.onNext(CANCELLED)");
                            throw new CancellationException();
                        } else {
                            Log.d(TAG, "Observer.onNext");
                            synchronized (lock) {
                                if (downloadProgress != latest) {
                                    latest = downloadProgress;
                                    notificationManager.updateNotification(boughtExcursion, downloadProgress);
                                }
                            }
                        }
                    }
                }, throwable -> {
                    if (isCancelled()) {
                        Log.d(TAG, "Observer.onError(CANCELLED)");
                        notificationManager.cancelNotification(boughtExcursion);
                    } else {
                        Log.d(TAG, "Observer.onError");
                        notificationManager.cancelNotification(boughtExcursion);
                        JobManagerEventContract.notifyException(applicationContext, getSingleIdWithoutPrefix());
                    }
                }, () -> {
                    if (isCancelled()) {
                        Log.d(TAG, "Observer.onSuccess(CANCELLED)");
                        notificationManager.cancelNotification(boughtExcursion);
                    } else {
                        Log.d(TAG, "Observer.onSuccess");
                        notificationManager.cancelNotification(boughtExcursion);
                        JobManagerEventContract.notifySuccess(applicationContext, getSingleIdWithoutPrefix());
                    }
                });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.d(TAG, "onCancel");
        injectDependenciesIfNotInjected();

        switch (cancelReason) {
            case CancelReason.CANCELLED_VIA_SHOULD_RE_RUN:
                if (notificationManager != null) {
                    notificationManager.cancelNotification(boughtExcursion);
                }
                JobManagerEventContract.notifyException(applicationContext, getSingleIdWithoutPrefix());
                break;
            case CancelReason.SINGLE_INSTANCE_ID_QUEUED:
            case CancelReason.SINGLE_INSTANCE_WHILE_RUNNING:
                int i = 0;
                // do nothing as another instance of this job exists in memory
                break;
            case CancelReason.CANCELLED_WHILE_RUNNING:
                if (notificationManager != null) {
                    notificationManager.cancelNotification(boughtExcursion);
                }
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

    private void injectDependenciesIfNotInjected() {
        if (excursionRepository == null || notificationManager == null || applicationContext == null) {
            Injector.getInstance()
                    .getDowndloadExcursionJobComponent()
                    .inject(this);
        }
    }

    private class CurrentThreadExecutor implements Executor {
        @Override
        public void execute(@NonNull Runnable command) {
            command.run();
        }
    }
}
