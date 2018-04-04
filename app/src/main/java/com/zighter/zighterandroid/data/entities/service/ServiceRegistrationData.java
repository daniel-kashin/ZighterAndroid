package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServiceRegistrationData implements Serializable {

    @SerializedName("email")
    @NonNull
    private String email;

    @SerializedName("first_name")
    @NonNull
    private String firstName;

    @SerializedName("last_name")
    @NonNull
    private String lastName;

    @SerializedName("password")
    @NonNull
    private String password;

    @SerializedName("username")
    @NonNull
    private String username;

    public ServiceRegistrationData(@NonNull String email,
                                   @NonNull String firstName,
                                   @NonNull String lastName,
                                   @NonNull String password,
                                   @NonNull String username) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
    }
}
