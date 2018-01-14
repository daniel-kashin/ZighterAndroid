package com.zighter.zighterandroid.util;

@SuppressWarnings("WeakerAccess")
public class LocationHelper {
    public static final int EARTH_RADIUS_IN_KM = 6371;

    public static double distanceInMeters(double latitude1,
                                          double latitude2,
                                          double longitude1,
                                          double longitude2) {
        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_IN_KM * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
