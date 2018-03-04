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

import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionIntentHelper.getCancelPendingIntent;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionIntentHelper.getOpenActivityPendingIntent;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.NOTIFICATION_CHANNEL_ID;

public class DownloadExcursionNotificationManager {
    @NonNull
    private final Context applicationContext;

    public DownloadExcursionNotificationManager(@NonNull Context applicationContext) {
        this.applicationContext = applicationContext.getApplicationContext();
    }

    void updateNotification(@NonNull BoughtExcursion boughtExcursion,
                            @Nullable DownloadProgress downloadProgress) {
        NotificationManager notificationManager = getNotificationManager(applicationContext);

        createNotificationChannel(applicationContext, notificationManager);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(boughtExcursion.getName())
                .setContentText(boughtExcursion.getOwner())
                .setContentIntent(getOpenActivityPendingIntent(applicationContext))
                .addAction(0, applicationContext.getString(R.string.cancel), getCancelPendingIntent(applicationContext, boughtExcursion))
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_my_location_black)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        if (downloadProgress != null && downloadProgress.getCount() != 0) {
            builder.setProgress(downloadProgress.getCount(), downloadProgress.getCurrentPosition() + 1, false);
        } else {
            builder.setProgress(1, 0, true);
        }

        notificationManager.notify(DownloadExcursionNotificationContract.NOTIFICATION_ID, builder.build());
    }

    void cancelNotification() {
        NotificationManager notificationManager = getNotificationManager(applicationContext);
        notificationManager.cancel(DownloadExcursionNotificationContract.NOTIFICATION_ID);
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
            channel.enableVibration(false);
            channel.enableLights(false);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
