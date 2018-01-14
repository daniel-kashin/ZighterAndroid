package com.zighter.zighterandroid.util;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import java.util.concurrent.TimeUnit;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class LocationListenerHolder {
    private static final long MIN_LOCATION_REQUEST_TIME = TimeUnit.SECONDS.toMillis(1);
    private static final long MIN_LOCATION_REQUEST_DISTANCE_IN_METERS = 1;

    @NonNull
    private final ProviderAwareLocationListener gpsLocationListener;
    @NonNull
    private final ProviderAwareLocationListener networkLocationListener;
    @NonNull
    private final OnLocationChangeListener innerListener;
    @NonNull
    private final LocationManager locationManager;
    private boolean areLocationUpdatesActive = false;

    @UiThread
    public LocationListenerHolder(@NonNull OnLocationChangeListener innerListener,
                                  @NonNull LocationManager locationManager) {
        this.innerListener = innerListener;
        this.locationManager = locationManager;
        gpsLocationListener = new ProviderAwareLocationListener(innerListener, locationManager, GPS_PROVIDER);
        networkLocationListener = new ProviderAwareLocationListener(innerListener, locationManager, NETWORK_PROVIDER);
    }

    @UiThread
    public void startLocationUpdates() {
        if (!areLocationUpdatesActive) {
            areLocationUpdatesActive = true;

            Location location = LocationSettingsHelper.getLastKnownLocation(locationManager);
            if (location != null) {
                innerListener.onLocationChanged(location);
            }

            startLocationUpdates(gpsLocationListener);
            startLocationUpdates(networkLocationListener);
        }
    }

    @UiThread
    public void stopLocationUpdates() {
        if (areLocationUpdatesActive) {
            areLocationUpdatesActive = false;
            stopLocationUpdates(gpsLocationListener);
            stopLocationUpdates(networkLocationListener);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(@NonNull ProviderAwareLocationListener listener) {
        locationManager.requestLocationUpdates(listener.getProvider(),
                                               MIN_LOCATION_REQUEST_TIME,
                                               MIN_LOCATION_REQUEST_DISTANCE_IN_METERS,
                                               listener);
    }

    private void stopLocationUpdates(@NonNull LocationListener locationListener) {
        locationManager.removeUpdates(locationListener);
    }

    private static class ProviderAwareLocationListener implements LocationListener {
        @NonNull
        private final OnLocationChangeListener innerListener;
        @NonNull
        private final LocationManager locationManager;
        @NonNull
        private final String provider;

        ProviderAwareLocationListener(@NonNull OnLocationChangeListener innerListener,
                                      @NonNull LocationManager locationManager,
                                      @NonNull String provider) {
            this.innerListener = innerListener;
            this.locationManager = locationManager;
            this.provider = provider;
        }

        @NonNull
        String getProvider() {
            return provider;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null && LocationSettingsHelper.shouldHandleLocation(locationManager, provider)) {
                innerListener.onLocationChanged(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    public interface OnLocationChangeListener {
        void onLocationChanged(@NonNull Location location);
    }
}
