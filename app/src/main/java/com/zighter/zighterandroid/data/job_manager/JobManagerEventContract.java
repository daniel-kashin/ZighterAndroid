package com.zighter.zighterandroid.data.job_manager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class JobManagerEventContract {
    static final String ACTION_EXCEPTION = "ACTION_EXCEPTION";
    static final String ACTION_SUCCESS = "ACTION_SUCCESS";
    static final String ACTION_STARTED = "ACTION_STARTED";
    static final String ACTION_CANCELLED = "ACTION_CANCELLED";
    static final String ACTION_ADDED = "ACTION_ADDED";
    static final String EXTRA_JOB_SINGLE_ID_WITHOUT_PREFIX = "EXTRA_BOUGHT_EXCURSION";

    public static void notifyAdded(@NonNull Context context, @NonNull String jobId) {
        notifyInner(context, ACTION_ADDED, jobId);
    }

    public static void notifyException(@NonNull Context context, @NonNull String jobId) {
        notifyInner(context, ACTION_EXCEPTION, jobId);
    }

    public static void notifySuccess(@NonNull Context context, @NonNull String jobId) {
        notifyInner(context, ACTION_SUCCESS, jobId);
    }

    public static void notifyStarted(@NonNull Context context, @NonNull String jobId) {
        notifyInner(context, ACTION_STARTED, jobId);
    }

    public static void notifyCancelled(@NonNull Context context, @NonNull String jobId) {
        notifyInner(context, ACTION_CANCELLED, jobId);
    }

    private static void notifyInner(@NonNull Context context,
                                    @NonNull String action,
                                    @NonNull String jobId) {
        Intent intent = new Intent(action).setPackage(context.getPackageName());
        intent.putExtra(EXTRA_JOB_SINGLE_ID_WITHOUT_PREFIX, jobId);
        context.sendBroadcast(intent);
    }
}
