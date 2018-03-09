package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ServiceSight extends Validable<ServiceSight> {
    @SerializedName("id")
    private String uuid;

    @SerializedName("point")
    private ServicePoint point;

    @SerializedName("description")
    private String description;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    private ServiceSight(@NonNull String uuid,
                         @NonNull ServicePoint point,
                         @Nullable String name,
                         @Nullable String type,
                         @Nullable String description) {
        this.uuid = uuid;
        this.point = point;
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isValid() {
        return point != null && point.isValid() && uuid != null && !uuid.equals("");
    }

    @Override
    public ServiceSight tryGetValid(boolean copy) {
        if (point == null || uuid == null || uuid.equals("")) return null;

        ServicePoint pointCopy = point.tryGetValid(true);
        if (pointCopy != null) {
            return new ServiceSight(uuid, pointCopy, name, type, description);
        } else {
            return null;
        }
    }

    @Nullable
    public String getName() {
        assertValid();
        return name;
    }

    @Nullable
    public String getType() {
        assertValid();
        return type;
    }

    @NonNull
    public String getUuid() {
        assertValid();
        return uuid;
    }

    public double getLongitude() {
        assertValid();
        return point.getLongitude();
    }

    public double getLatitude() {
        assertValid();
        return point.getLatitude();
    }

    @Nullable
    public String getDescription() {
        assertValid();
        return description;
    }
}
