package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ServiceVideo extends Validable<ServiceVideo> {
    @SerializedName("description")
    String description;

    @SerializedName("title")
    String title;

    @SerializedName("url")
    String url;

    public ServiceVideo(@NonNull String url,
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
    public ServiceVideo tryGetValid(boolean copy) {
        if (isValid()) {
            if (copy) {
                return new ServiceVideo(url, description, title);
            } else {
                return this;
            }
        }

        return null;
    }
}
