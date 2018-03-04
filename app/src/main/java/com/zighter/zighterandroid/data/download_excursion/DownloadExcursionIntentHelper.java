package com.zighter.zighterandroid.data.download_excursion;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.presentation.bottom_navigation.BottomNavigationActivity;

import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.ACTION_CANCEL_JOB;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.ACTION_OPEN_ACTIVITY;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.EXTRA_EXCURSION;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.REQUEST_CODE_CANCEL_JOB;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.REQUEST_CODE_OPEN_ACTIVITY;

public class DownloadExcursionIntentHelper {
    @NonNull
    public static Intent getCancelIntent(@NonNull Context context,
                                         @NonNull BoughtExcursion boughtExcursion) {
        return new Intent(ACTION_CANCEL_JOB)
                .putExtra(EXTRA_EXCURSION, boughtExcursion)
                .setClass(context, DownloadExcursionNotificationBroadcastReceiver.class);
    }

    @NonNull
    public static PendingIntent getCancelPendingIntent(@NonNull Context context,
                                                       @NonNull BoughtExcursion boughtExcursion) {

        return PendingIntent.getBroadcast(context,
                                          REQUEST_CODE_CANCEL_JOB,
                                          getCancelIntent(context, boughtExcursion),
                                          PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @NonNull
    public static PendingIntent getOpenActivityPendingIntent(@NonNull Context context) {
        Intent intent = new Intent(ACTION_OPEN_ACTIVITY);
        intent.setClass(context, BottomNavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return PendingIntent.getActivity(context,
                                         REQUEST_CODE_OPEN_ACTIVITY,
                                         intent,
                                         PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
