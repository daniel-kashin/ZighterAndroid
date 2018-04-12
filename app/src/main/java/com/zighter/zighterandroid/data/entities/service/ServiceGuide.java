package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

public class ServiceGuide extends Validable<ServiceGuide> {
    @SerializedName("id")
    String uuid;

    @SerializedName("username")
    String username;

    @SerializedName("first_name")
    String firstName;

    @SerializedName("last_name")
    String lastName;

    @SerializedName("phone")
    String phone;

    @SerializedName("email")
    String email;

    @VisibleForTesting
    public ServiceGuide(@NonNull String uuid,
                        @NonNull String username,
                        @NonNull String firstName,
                        @Nullable String lastName,
                        @NonNull String phone,
                        @NonNull String email) {
        this.uuid = uuid;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public void setUuid(@NonNull String uuid) {
        this.uuid = uuid;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    @Nullable
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @Override
    public boolean isValid() {
        return  uuid != null && username != null && firstName != null && phone != null && email != null;
    }

    @Nullable
    @Override
    public ServiceGuide tryGetValid(boolean copy) {
        if (!isValid()) {
            return null;
        }

        return copy
                ? new ServiceGuide(uuid, username, firstName, lastName, phone, email)
                : this;
    }
}
