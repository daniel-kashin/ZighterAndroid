package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ServiceAudio extends Validable<ServiceAudio> {
    @SerializedName("title")
    String title;

    @SerializedName("url")
    String url;

    public ServiceAudio(@NonNull String url,
                        @Nullable String title) {
        this.url = url;
        this.title = title;
    }

    @Override
    public boolean isValid() {
        return url != null;
    }

    @Nullable
    @Override
    public ServiceAudio tryGetValid(boolean copy) {
        if (isValid()) {
            if (copy) {
                return new ServiceAudio(url, title);
            } else {
                return this;
            }
        }

        return null;
    }
}