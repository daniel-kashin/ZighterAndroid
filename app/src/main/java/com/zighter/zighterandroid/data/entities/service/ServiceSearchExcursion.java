package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.zighter.zighterandroid.data.repositories.ZighterEndpoints;

public class ServiceSearchExcursion extends Validable<ServiceSearchExcursion> {
    @SerializedName("id")
    String uuid;

    @SerializedName("name")
    String name;

    @SerializedName("image_url")
    String imageUrl;

    @SerializedName("location")
    String location;

    @SerializedName("provider_username")
    String providerUsername;

    @SerializedName("has_route")
    boolean hasRoute;

    @SerializedName("has_media")
    boolean hasMedia;

    @SerializedName("has_guide")
    boolean hasGuide;

    @SerializedName("route_price")
    Double routePrice;

    @SerializedName("media_price")
    Double mediaPrice;

    @SerializedName("guide_price")
    Double guidePrice;

    private ServiceSearchExcursion(@NonNull String uuid,
                                   @NonNull String name,
                                   @NonNull String imageUrl,
                                   @NonNull String location,
                                   @NonNull String providerUsername,
                                   @Nullable Double routePrice,
                                   @Nullable Double mediaPrice,
                                   @Nullable Double guidePrice) {
        this.uuid = uuid;
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
        this.providerUsername = providerUsername;
        this.routePrice = routePrice;
        this.mediaPrice = mediaPrice;
        this.guidePrice = guidePrice;
        this.hasGuide = true;
        this.hasRoute = true;
        this.hasMedia = true;
    }

    @Override
    public boolean isValid() {
        return uuid != null
                && name != null
                && imageUrl != null
                && location != null
                && providerUsername != null
                && (routePrice != null || mediaPrice != null || guidePrice != null);
    }

    @Nullable
    @Override
    public ServiceSearchExcursion tryGetValid(boolean copy) {
        if (!isValid()) {
            return null;
        }

        if (copy) {
            return new ServiceSearchExcursion(uuid, name, imageUrl, location, providerUsername, routePrice, mediaPrice, guidePrice);
        } else {
            return this;
        }
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
    public String getImageUrl() {
        return ZighterEndpoints.BASE_URL + imageUrl;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    @NonNull
    public String getProviderUsername() {
        return providerUsername;
    }

    @Nullable
    public Double getRoutePrice() {
        if (!hasRoute) {
            return null;
        }

        return routePrice;
    }

    @Nullable
    public Double getMediaPrice() {
        if (!hasMedia) {
            return null;
        }

        return mediaPrice;
    }

    @Nullable
    public Double getGuidePrice() {
        if (!hasGuide) {
            return null;
        }

        return guidePrice;
    }
}
