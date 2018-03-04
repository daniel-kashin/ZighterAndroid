package com.zighter.zighterandroid.data.job_manager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;

public class JobManagerProgressContract {
    public static final String ACTION_EXCEPTION = "ACTION_EXCEPTION";
    public static final String ACTION_SUCCESS = "ACTION_SUCCESS";
    public static final String ACTION_STARTED = "ACTION_STARTED";
    public static final String ACTION_CANCELLED = "ACTION_CANCELLED";
    public static final String ACTION_ADDED = "ACTION_ADDED";
    public static final String EXTRA_EXCURSION = "EXTRA_BOUGHT_EXCURSION";

    public static void notifyAdded(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_ADDED);
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }

    public static void notifyException(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_EXCEPTION);
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }

    public static void notifySuccess(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_SUCCESS).setPackage(context.getPackageName());
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }

    public static void notifyStarted(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_STARTED);
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }

    public static void notifyCancelled(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_CANCELLED);
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }
}
