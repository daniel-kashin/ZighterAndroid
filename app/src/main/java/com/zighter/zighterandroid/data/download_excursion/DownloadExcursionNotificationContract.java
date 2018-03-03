package com.zighter.zighterandroid.data.download_excursion;

public interface DownloadExcursionNotificationContract {
    int REQUEST_CODE_OPEN_ACTIVITY = 200;
    String ACTION_OPEN_ACTIVITY = "ACTION_OPEN_ACTIVITY";
    int REQUEST_CODE_CANCEL_JOB = 199;
    String ACTION_CANCEL_JOB = "ACTION_CANCEL_DOWNLOAD_EXCURSION_JOB";
    String EXTRA_EXCURSION = "EXTRA_BOUGHT_EXCURSION";
    int NOTIFICATION_ID = 158;
    String NOTIFICATION_CHANNEL_ID = "158";
}
