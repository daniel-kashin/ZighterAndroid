package com.zighter.zighterandroid.data.download_excursion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.repositories.excursion.DownloadProgress;
import com.zighter.zighterandroid.presentation.bottom_navigation.BottomNavigationActivity;

import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.ACTION_CANCEL_JOB;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.ACTION_OPEN_ACTIVITY;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.NOTIFICATION_CHANNEL_ID;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.EXTRA_EXCURSION;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.REQUEST_CODE_CANCEL_JOB;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.REQUEST_CODE_OPEN_ACTIVITY;

public class DownloadExcursionNotificationManager {
    @NonNull
    private final Context applicationContext;

    public DownloadExcursionNotificationManager(@NonNull Context applicationContext) {
        this.applicationContext = applicationContext.getApplicationContext();
    }

    public void updateNotification(@NonNull BoughtExcursion boughtExcursion,
                                   @Nullable DownloadProgress downloadProgress) {
        NotificationManager notificationManager = getNotificationManager(applicationContext);
        PendingIntent cancelPendingIntent = getCancelPendingIntent(applicationContext, boughtExcursion);

        createNotificationChannel(applicationContext, notificationManager);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(boughtExcursion.getName())
                .setContentText(boughtExcursion.getOwner())
                .setContentIntent(getOpenActivityPendingIntent(applicationContext))
                .addAction(0, applicationContext.getString(R.string.cancel), cancelPendingIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_my_location_black)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        if (downloadProgress != null && downloadProgress.getCount() != 0) {
            builder.setProgress(downloadProgress.getCount(), downloadProgress.getCurrentPosition() + 1, false);
        }

        notificationManager.notify(DownloadExcursionNotificationContract.NOTIFICATION_ID, builder.build());
    }

    public void cancelNotification() {
        NotificationManager notificationManager = getNotificationManager(applicationContext);
        notificationManager.cancel(DownloadExcursionNotificationContract.NOTIFICATION_ID);
    }

    @NonNull
    private static NotificationManagerCompat getNotificationManagerCompat(@NonNull Context context) {
        //noinspection ConstantConditions
        return NotificationManagerCompat.from(context);
    }

    @NonNull
    private static NotificationManager getNotificationManager(@NonNull Context context) {
        //noinspection ConstantConditions
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private static void createNotificationChannel(@NonNull Context context, @NonNull NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                                                                  context.getString(R.string.download_excursion),
                                                                  NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @NonNull
    private static PendingIntent getCancelPendingIntent(@NonNull Context context,
                                                        @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(ACTION_CANCEL_JOB)
                .putExtra(EXTRA_EXCURSION, boughtExcursion)
                .setClass(context, DownloadExcursionNotificationBroadcastReceiver.class);

        return PendingIntent.getBroadcast(context,
                                          REQUEST_CODE_CANCEL_JOB,
                                          intent,
                                          PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @NonNull
    private static PendingIntent getOpenActivityPendingIntent(@NonNull Context context) {
        Intent intent = new Intent(ACTION_OPEN_ACTIVITY);
        intent.setClass(context, BottomNavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return PendingIntent.getActivity(context,
                                  REQUEST_CODE_OPEN_ACTIVITY,
                                  intent,
                                  PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
