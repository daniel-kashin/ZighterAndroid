package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ServicePoint extends Validable<ServicePoint> {

    @SerializedName("lng")
    private double longitude;

    @SerializedName("lat")
    private double latitude;

    private ServicePoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean isValid() {
        return longitude != 0.0 && latitude != 0.0;
    }

    @Override
    @Nullable
    public ServicePoint tryGetValid(boolean copy) {
        if (isValid()) {
            return copy ? new ServicePoint(longitude, latitude) : this;
        } else {
            return null;
        }
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
