package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.zighter.zighterandroid.data.repositories.ZighterEndpoints;

public class ServiceImage extends Validable<ServiceImage> {
    @SerializedName("description")
    String description;

    @SerializedName("title")
    String title;

    @SerializedName("url")
    String url;

    public ServiceImage(@NonNull String url,
                        @Nullable String description,
                        @Nullable String title) {
        this.url = url;
        this.description = description;
        this.title = title;
    }

    @Override
    public boolean isValid() {
        return url != null;
    }

    @Nullable
    @Override
    public ServiceImage tryGetValid(boolean copy) {
        if (isValid()) {
            if (copy) {
                return new ServiceImage(url, description, title);
            } else {
                return this;
            }
        }

        return null;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getUrl() {
        return ZighterEndpoints.BASE_URL + url;
    }
}
