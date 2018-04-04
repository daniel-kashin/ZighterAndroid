package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceLoginData implements Serializable {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public ServiceLoginData(@NonNull String username,
                            @NonNull String password) {
        this.username = username;
        this.password = password;
    }
}
