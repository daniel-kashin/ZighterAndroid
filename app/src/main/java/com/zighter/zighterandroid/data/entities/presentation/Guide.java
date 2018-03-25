package com.zighter.zighterandroid.data.entities.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.io.Serializable;

public class Guide implements Serializable {
    @NonNull
    String uuid;
    @NonNull
    String username;
    @NonNull
    String firstName;
    @Nullable
    String lastName;
    @NonNull
    String phone;
    @NonNull
    String email;

    public Guide(@NonNull String uuid,
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
}
