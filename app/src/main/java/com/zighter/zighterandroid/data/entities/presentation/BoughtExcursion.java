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
    @NonNull
    private Image image;
    private boolean isRouteAvailable;
    private boolean isMediaAvailable;
    private boolean isGuideAvailable;
    private boolean isGuideSaved;
    private boolean isMediaSaved;
    private boolean isRouteSaved;

    public BoughtExcursion(@NonNull String uuid,
                           @NonNull String name,
                           @NonNull String owner,
                           @NonNull String location,
                           @NonNull Image image,
                           boolean isGuideAvailable,
                           boolean isMediaAvailable,
                           boolean isRouteAvailable,
                           boolean isGuideSaved,
                           boolean isMediaSaved,
                           boolean isRouteSaved) {
        this.uuid = uuid;
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.image = image;
        this.isGuideAvailable = isGuideAvailable;
        this.isRouteAvailable = isRouteAvailable;
        this.isMediaAvailable = isMediaAvailable;
        this.isGuideSaved = isGuideSaved;
        this.isMediaSaved = isMediaSaved;
        this.isRouteSaved = isRouteSaved;
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

    @NonNull
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

    public boolean isGuideSaved() {
        return isGuideSaved;
    }

    public boolean isMediaSaved() {
        return isMediaSaved;
    }

    public boolean isRouteSaved() {
        return isRouteSaved;
    }

    public boolean isSavedAllAvailable() {
        return (!isRouteAvailable || isRouteSaved) && (!isGuideAvailable || isGuideSaved) && (!isMediaAvailable || isMediaSaved);
    }
}
