package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ServiceToken extends Validable<ServiceToken> {
    @SerializedName("token")
    private String token;

    private ServiceToken(@NonNull String token) {
        this.token = token;
    }

    @NonNull
    public String getToken() {
        return token;
    }

    @Override
    public boolean isValid() {
        return token != null;
    }

    @Nullable
    @Override
    public ServiceToken tryGetValid(boolean copy) {
        if (token == null) {
            return null;
        }

        return copy ? new ServiceToken(token) : this;
    }
}
