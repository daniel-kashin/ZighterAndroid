package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.entities.media.Media;

import java.io.Serializable;
import java.util.List;

public class Sight implements Serializable {
    @NonNull
    private String uuid;
    private double longitude;
    private double latitude;
    @Nullable
    private String description;
    @Nullable
    private String name;
    @Nullable
    private String type;
    @NonNull
    private List<Media> medias;

    Sight(@NonNull String uuid,
                  double longitude,
                  double latitude,
                  @Nullable String name,
                  @Nullable String type,
                  @Nullable String description,
                  @NonNull List<Media> medias) {
        this.uuid = uuid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.type = type;
        this.medias = medias;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public int getMediaSize() {
        return medias.size();
    }

    @NonNull
    public Media getMediaAt(int index) {
        return medias.get(index);
    }
}
