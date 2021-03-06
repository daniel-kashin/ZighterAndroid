package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.zighter.zighterandroid.data.repositories.ZighterEndpoints;
import com.zighter.zighterandroid.util.BooleanHelper;

import static com.zighter.zighterandroid.util.BooleanHelper.toBoolean;

@SuppressWarnings("WeakerAccess")
public class ServiceBoughtExcursion extends Validable<ServiceBoughtExcursion> {
    @SerializedName("excursion_id")
    @Nullable
    String uuid;
    @SerializedName("excursion_name")
    @Nullable
    String name;
    @SerializedName("provider_username")
    @Nullable
    String owner;
    @SerializedName("provider_id")
    @Nullable
    String ownerUuid;
    @SerializedName("excursion_location")
    @Nullable
    String location;
    @SerializedName("excursion_image_url")
    @Nullable
    String imageUrl;
    @SerializedName("has_route")
    @Nullable
    String isRouteAvailable;
    @SerializedName("has_media")
    @Nullable
    String isMediaAvailable;
    @SerializedName("has_guide")
    @Nullable
    String isGuideAvailable;

    public ServiceBoughtExcursion(@NonNull String uuid,
                                  @NonNull String name,
                                  @NonNull String owner,
                                  @NonNull String ownerUuid,
                                  @NonNull String location,
                                  @Nullable String imageUrl,
                                  boolean isRouteAvailable,
                                  boolean isMediaAvailable,
                                  boolean isGuideAvailable) {
        this.uuid = uuid;
        this.name = name;
        this.owner = owner;
        this.ownerUuid = ownerUuid;
        this.location = location;
        this.imageUrl = imageUrl;
        this.isRouteAvailable = BooleanHelper.toString(isRouteAvailable);
        this.isMediaAvailable = BooleanHelper.toString(isMediaAvailable);
        this.isGuideAvailable = BooleanHelper.toString(isGuideAvailable);
    }

    @NonNull
    public String getUuid() {
        if (uuid == null) throw new IllegalStateException();
        return uuid;
    }

    @NonNull
    public String getName() {
        if (name == null) throw new IllegalStateException();
        return name;
    }

    @NonNull
    public String getLocation() {
        if (location == null) throw new IllegalStateException();
        return location;
    }

    @NonNull
    public String getOwner() {
        if (owner == null) throw new IllegalStateException();
        return owner;
    }

    @NonNull
    public String getOwnerUuid() {
        if (ownerUuid == null) throw new IllegalStateException();
        return ownerUuid;
    }

    @NonNull
    public String getImageUrl() {
        return ZighterEndpoints.BASE_URL + imageUrl;
    }

    public boolean isRouteAvailable() {
        return isRouteAvailable != null && toBoolean(isRouteAvailable);
    }

    public boolean isMediaAvailable() {
        return isMediaAvailable != null && toBoolean(isMediaAvailable);
    }

    public boolean isGuideAvailable() {
        return isGuideAvailable != null && toBoolean(isGuideAvailable);
    }

    @Override
    public boolean isValid() {
        return (isRouteAvailable() || isGuideAvailable() || isMediaAvailable()) &&
                uuid != null && name != null && location != null && owner != null && ownerUuid != null && imageUrl != null;
    }

    @Nullable
    @Override
    public ServiceBoughtExcursion tryGetValid(boolean copy) {
        if (!isValid()) {
            return null;
        }

        if (copy) {
            return new ServiceBoughtExcursion(uuid,
                                              name,
                                              owner,
                                              ownerUuid,
                                              location,
                                              imageUrl,
                                              isRouteAvailable(),
                                              isMediaAvailable(),
                                              isGuideAvailable());
        }

        return this;
    }
}
