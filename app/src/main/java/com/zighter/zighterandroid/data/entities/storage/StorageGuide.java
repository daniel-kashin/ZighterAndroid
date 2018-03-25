package com.zighter.zighterandroid.data.entities.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType;
import com.zighter.zighterandroid.data.database.StorageGuideContract;

@StorIOSQLiteType(table = StorageGuideContract.TABLE_NAME)
public class StorageGuide {

    @StorIOSQLiteColumn(name = StorageGuideContract.COLUMN_ID, key = true)
    Long id = null;

    @StorIOSQLiteColumn(name = StorageGuideContract.COLUMN_UUID)
    String uuid;

    @StorIOSQLiteColumn(name = StorageGuideContract.COLUMN_USERNAME)
    String username;

    @StorIOSQLiteColumn(name = StorageGuideContract.COLUMN_FIRST_NAME)
    String firstName;

    @StorIOSQLiteColumn(name = StorageGuideContract.COLUMN_LAST_NAME)
    String lastName;

    @StorIOSQLiteColumn(name = StorageGuideContract.COLUMN_PHONE)
    String phone;

    @StorIOSQLiteColumn(name = StorageGuideContract.COLUMN_EMAIL)
    String email;

    StorageGuide() {
    }

    public StorageGuide(@NonNull String uuid,
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

    @Nullable
    public Long getId() {
        return id;
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