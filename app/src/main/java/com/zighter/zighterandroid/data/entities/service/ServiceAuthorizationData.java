package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ServiceAuthorizationData extends Validable<ServiceAuthorizationData> {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public ServiceAuthorizationData(@NonNull String username,
                                    @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Nullable
    @Override
    public ServiceAuthorizationData tryGetValid(boolean copy) {
        return null;
    }
}
