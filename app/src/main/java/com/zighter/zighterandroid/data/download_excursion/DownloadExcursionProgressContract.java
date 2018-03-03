package com.zighter.zighterandroid.data.download_excursion;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;

public class DownloadExcursionProgressContract {
    public static final String ACTION_DOWNLOAD_EXCEPTION = "ACTION_DOWNLOAD_EXCEPTION";
    public static final String ACTION_DOWNLOAD_SUCCESS = "ACTION_DOWNLOAD_SUCCESS";
    public static final String ACTION_DOWNLOAD_STARTED = "ACTION_DOWNLOAD_STARTED";
    public static final String ACTION_DOWNLOAD_CANCELLED = "ACTION_DOWNLOAD_CANCELLED";
    public static final String EXTRA_EXCURSION = "EXTRA_BOUGHT_EXCURSION";

    static void notifyException(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_DOWNLOAD_EXCEPTION);
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }

    static void notifySuccess(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_DOWNLOAD_SUCCESS).setPackage(context.getPackageName());
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }

    static void notifyStarted(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_DOWNLOAD_STARTED);
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }

    static void notifyCancelled(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_DOWNLOAD_CANCELLED);
        intent.putExtra(EXTRA_EXCURSION, boughtExcursion);
        context.sendBroadcast(intent);
    }
}
