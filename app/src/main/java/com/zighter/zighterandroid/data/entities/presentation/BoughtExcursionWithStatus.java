package com.zighter.zighterandroid.data.entities.presentation;

import android.support.annotation.NonNull;

public class BoughtExcursionWithStatus {

    @NonNull
    private final BoughtExcursion boughtExcursion;
    @NonNull
    private DownloadStatus downloadStatus;

    public BoughtExcursionWithStatus(@NonNull BoughtExcursion boughtExcursion,
                                     @NonNull DownloadStatus downloadStatus) {
        this.boughtExcursion = boughtExcursion;
        this.downloadStatus = downloadStatus;
    }

    @NonNull
    public BoughtExcursion getExcursion() {
        return boughtExcursion;
    }

    @NonNull
    public DownloadStatus getStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(@NonNull DownloadStatus downloadStatus) {
        this.downloadStatus = downloadStatus;
    }


    public enum DownloadStatus {
        DOWNLOADED,
        DOWNLOADING,
        IDLE
    }
}
