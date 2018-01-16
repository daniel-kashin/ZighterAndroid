package com.zighter.zighterandroid.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static android.location.LocationManager.PASSIVE_PROVIDER;

@SuppressWarnings("WeakerAccess")
public class LocationSettingsHelper {
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1223;
    public static final int LOCATION_PROVIDER_REQUEST_CODE = 1224;

    public static boolean isLocationPermissionGranted(@NonNull Context context) {
        int locationPermission = ContextCompat.checkSelfPermission(context.getApplicationContext(),
                                                                   LOCATION_PERMISSION);
        return locationPermission == PackageManager.PERMISSION_GRANTED;
    }

    public static void showLocationPermissionRationale(@NonNull Activity context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0) {
            // TODO
            context.startActivityForResult(intent, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // TODO
            Toast.makeText(context, "PLS", Toast.LENGTH_SHORT).show();
        }
    }

    public static void requestLocationPermission(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(activity,
                                          new String[]{LOCATION_PERMISSION},
                                          LOCATION_PERMISSION_REQUEST_CODE);
    }

    public static void openGpsSettings(@NonNull Activity activity) {
        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(gpsOptionsIntent, LOCATION_PROVIDER_REQUEST_CODE);
    }

    @Nullable
    public static Location getLastKnownLocation(@NonNull LocationManager locationManager) throws SecurityException {
        Location location = locationManager.getLastKnownLocation(GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(NETWORK_PROVIDER);
        } else {
            location = locationManager.getLastKnownLocation(PASSIVE_PROVIDER);
        }
        return location;
    }

    public static boolean shouldHandleLocation(@NonNull LocationManager locationManager, @NonNull String provider) {
        boolean result = false;
        switch (provider) {
            case GPS_PROVIDER:
                result = true;
                break;
            case NETWORK_PROVIDER:
                if (!locationManager.isProviderEnabled(GPS_PROVIDER)) {
                    result = true;
                }
                break;
        }
        return result;
    }

    public static boolean isGpsLocationEnabled(@NonNull LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetworkLocationEnabled(@NonNull LocationManager locationManager) {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressWarnings("ConstantConditions")
    public static void ensureGpsEnabled(@NonNull Activity activity) {
        if (!LocationSettingsHelper.isGpsLocationEnabled((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE))) {
            // TODO
            LocationSettingsHelper.openGpsSettings(activity);
        }
    }
}
