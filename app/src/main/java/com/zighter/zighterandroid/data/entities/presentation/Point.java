package com.zighter.zighterandroid.data.entities.presentation;

public class Point {
    private double longitude;
    private double latitude;

    public Point(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
