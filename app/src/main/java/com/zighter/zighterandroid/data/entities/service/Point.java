package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Point implements Validable<Point>, Serializable {

    @SerializedName("lng")
    private double longitude;

    @SerializedName("lat")
    private double latitude;

    private Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean isValid() {
        return longitude != 0 && latitude != 0;
    }

    @Override
    public Point tryGetValidCopy() {
        return isValid()
            ? new Point(longitude, latitude)
            : null;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
