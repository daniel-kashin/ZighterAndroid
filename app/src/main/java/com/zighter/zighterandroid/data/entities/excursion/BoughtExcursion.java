package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BoughtExcursion implements Serializable {
    @SerializedName("excursion_id")
    String uuid;
    @SerializedName("excursion__name")
    String name;
    @SerializedName("user__username")
    String owner;
    @SerializedName("excursion__location")
    String location;

    public BoughtExcursion(@NonNull String uuid,
                           @Nullable String name,
                           @Nullable String owner,
                           @Nullable String location) {
        this.uuid = uuid;
        this.name = name;
        this.owner = owner;
        this.location = location;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getOwner() {
        return owner;
    }

}
