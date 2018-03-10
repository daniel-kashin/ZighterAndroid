package com.zighter.zighterandroid.data.entities.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.repositories.ZighterEndpoints;

import java.io.Serializable;

public class BoughtExcursion implements Serializable {
    @NonNull
    private String uuid;
    @NonNull
    private String name;
    @NonNull
    private String owner;
    @NonNull
    private String location;
    @Nullable
    private Image image;
    private boolean isRouteAvailable;
    private boolean isMediaAvailable;
    private boolean isGuideAvailable;
    private boolean isFullySaved;

    public BoughtExcursion(@NonNull String uuid,
                           @NonNull String name,
                           @NonNull String owner,
                           @NonNull String location,
                           @Nullable Image image,
                           boolean isGuideAvailable,
                           boolean isMediaAvailable,
                           boolean isRouteAvailable,
                           boolean isFullySaved) {
        this.uuid = uuid;
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.image = image;
        this.isGuideAvailable = isGuideAvailable;
        this.isRouteAvailable = isRouteAvailable;
        this.isMediaAvailable = isMediaAvailable;
        this.isFullySaved = isFullySaved;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    @NonNull
    public String getOwner() {
        return owner;
    }

    @Nullable
    public Image getImage() {
        return image;
    }

    public boolean isRouteAvailable() {
        return isRouteAvailable;
    }

    public boolean isMediaAvailable() {
        return isMediaAvailable;
    }

    public boolean isGuideAvailable() {
        return isGuideAvailable;
    }

    public boolean isFullySaved() {
        return isFullySaved;
    }
}
