package com.zighter.zighterandroid.data.job_manager.download_excursion;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.repositories.excursion.DownloadProgress;

import static com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationContract.NOTIFICATION_CHANNEL_ID;
import static com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationContract.getCancelPendingIntent;
import static com.zighter.zighterandroid.data.job_manager.download_excursion.DownloadExcursionNotificationContract.getMainPendingIntent;

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
                .setContentIntent(getMainPendingIntent(applicationContext))
                .addAction(0, applicationContext.getString(R.string.cancel), getCancelPendingIntent(applicationContext, boughtExcursion))
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_file_download_black)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        if (downloadProgress == null) {
            builder.setContentText(applicationContext.getString(R.string.loading));
            builder.setProgress(1, 0, true);
        } else {
            DownloadProgress.Type type = downloadProgress.getType();
            if (type == DownloadProgress.Type.MAP) {
                builder.setContentText(applicationContext.getString(R.string.loading_map));
            } else if (type == DownloadProgress.Type.JSON) {
                builder.setContentText(applicationContext.getString(R.string.loading_json));
            } else if (type == DownloadProgress.Type.MEDIA) {
                builder.setContentText(applicationContext.getString(R.string.loading_media));
            } else {
                builder.setContentText(applicationContext.getString(R.string.loading));
            }

            if (downloadProgress.getCount() == 0) {
                builder.setProgress(1, 0, true);
            } else {
                builder.setProgress(downloadProgress.getCount(), downloadProgress.getCurrentPosition() + 1, false);
            }
        }

        notificationManager.notify(boughtExcursion.getUuid(), DownloadExcursionNotificationContract.NOTIFICATION_ID, builder.build());
    }

    void cancelNotification(@NonNull BoughtExcursion boughtExcursion) {
        NotificationManager notificationManager = getNotificationManager(applicationContext);
        notificationManager.cancel(boughtExcursion.getUuid(), DownloadExcursionNotificationContract.NOTIFICATION_ID);
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
