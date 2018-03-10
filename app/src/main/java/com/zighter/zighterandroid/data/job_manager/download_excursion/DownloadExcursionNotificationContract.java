package com.zighter.zighterandroid.data.job_manager.download_excursion;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.presentation.bottom_navigation.BottomNavigationActivity;

public class DownloadExcursionNotificationContract {
    public static final int REQUEST_CODE_OPEN_ACTIVITY = 200;
    public static final String ACTION_OPEN_ACTIVITY = "ACTION_OPEN_ACTIVITY";
    public static final int REQUEST_CODE_CANCEL_JOB = 199;
    public static final String ACTION_CANCEL_JOB = "ACTION_CANCEL_DOWNLOAD_EXCURSION_JOB";
    public static final String EXTRA_EXCURSION = "EXTRA_BOUGHT_EXCURSION";
    public static final int NOTIFICATION_ID = 158;
    public static final String NOTIFICATION_CHANNEL_ID = "158";

    public static void cancel(@NonNull Context context,
                              @NonNull BoughtExcursion boughtExcursion) {
        context.sendBroadcast(getCancelIntent(context, boughtExcursion));
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
    public static PendingIntent getMainPendingIntent(@NonNull Context context) {
        Intent intent = new Intent(ACTION_OPEN_ACTIVITY);
        intent.setClass(context, BottomNavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return PendingIntent.getActivity(context,
                                         REQUEST_CODE_OPEN_ACTIVITY,
                                         intent,
                                         PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @NonNull
    private static Intent getCancelIntent(@NonNull Context context,
                                          @NonNull BoughtExcursion boughtExcursion) {
        return new Intent(ACTION_CANCEL_JOB)
                .putExtra(EXTRA_EXCURSION, boughtExcursion)
                .setClass(context, DownloadExcursionNotificationBroadcastReceiver.class);
    }
}
