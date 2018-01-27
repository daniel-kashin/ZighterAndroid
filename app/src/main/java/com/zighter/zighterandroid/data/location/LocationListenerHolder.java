package com.zighter.zighterandroid.data.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.zighter.zighterandroid.util.LocationSettingsHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class LocationListenerHolder {
    private static final long MIN_LOCATION_REQUEST_TIME = TimeUnit.SECONDS.toMillis(15);
    private static final long MIN_LOCATION_REQUEST_DISTANCE_IN_METERS = 15;

    @NonNull
    private final ProviderAwareLocationListener gpsLocationListener;
    @NonNull
    private final ProviderAwareLocationListener networkLocationListener;
    @NonNull
    private final List<OnLocationChangeListener> subscribers;
    @NonNull
    private final LocationManager locationManager;

    private boolean areLocationUpdatesActive;
    private boolean isGpsLocationEnabled;
    private boolean isNetworkLocationEnabled;

    @UiThread
    public LocationListenerHolder(@NonNull LocationManager locationManager) {
        this.locationManager = locationManager;

        this.subscribers = new ArrayList<>();
        gpsLocationListener = new ProviderAwareLocationListener(this, locationManager, GPS_PROVIDER);
        networkLocationListener = new ProviderAwareLocationListener(this, locationManager, NETWORK_PROVIDER);
    }

    @UiThread
    public void register(@NonNull OnLocationChangeListener subscriber) {
        if (!isRegistered(subscriber)) {
            boolean wasEmpty = subscribers.isEmpty();

            if (wasEmpty) updateProvidersAvailability();
            subscriber.onLocationProvidersAvailabilityChanged(isNetworkLocationEnabled, isGpsLocationEnabled);

            Location location = LocationSettingsHelper.getLastKnownLocation(locationManager);
            if (location != null) subscriber.onLocationChanged(location);

            subscribers.add(subscriber);

            if (wasEmpty) startLocationUpdates();
        }
    }

    @UiThread
    public void unregister(@NonNull OnLocationChangeListener subscriber) {
        if (isRegistered(subscriber)) {
            subscribers.remove(subscriber);
            if (subscribers.isEmpty()) {
                stopLocationUpdates();
            }
        }
    }

    @UiThread
    private boolean isRegistered(@NonNull OnLocationChangeListener subscriber) {
        return subscribers.contains(subscriber);
    }

    @UiThread
    private void startLocationUpdates() {
        if (!areLocationUpdatesActive) {
            areLocationUpdatesActive = true;
            startLocationUpdates(gpsLocationListener);
            startLocationUpdates(networkLocationListener);
        }
    }

    @UiThread
    private void stopLocationUpdates() {
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

    private void updateProvidersAvailability() {
        boolean isGpsLocationEnabledNew = LocationSettingsHelper.isGpsLocationEnabled(locationManager);
        boolean isNetworkLocationEnabledNew = LocationSettingsHelper.isNetworkLocationEnabled(locationManager);

        boolean updated = isGpsLocationEnabledNew != isGpsLocationEnabled
                || isNetworkLocationEnabledNew != isNetworkLocationEnabled;

        isGpsLocationEnabled = isGpsLocationEnabledNew;
        isNetworkLocationEnabled = isNetworkLocationEnabledNew;

        if (!subscribers.isEmpty() && updated) {
            for (OnLocationChangeListener listener : subscribers) {
                listener.onLocationProvidersAvailabilityChanged(isNetworkLocationEnabled, isGpsLocationEnabled);
            }
        }
    }

    private void updateProviderAvailability(@Nullable String provider) {
        boolean updated = false;
        if (GPS_PROVIDER.equals(provider)) {
            boolean isGpsLocationEnabledNew = LocationSettingsHelper.isGpsLocationEnabled(locationManager);
            updated = isGpsLocationEnabledNew != isGpsLocationEnabled;
            isGpsLocationEnabled = isGpsLocationEnabledNew;
        } else if (NETWORK_PROVIDER.equals(provider)) {
            boolean isNetworkLocationEnabledNew = LocationSettingsHelper.isNetworkLocationEnabled(locationManager);
            updated = isNetworkLocationEnabledNew != isNetworkLocationEnabled;
            isNetworkLocationEnabled = isNetworkLocationEnabledNew;
        }

        if (!subscribers.isEmpty() && updated) {
            for (OnLocationChangeListener listener : subscribers) {
                listener.onLocationProvidersAvailabilityChanged(isNetworkLocationEnabled, isGpsLocationEnabled);
            }
        }
    }

    private static class ProviderAwareLocationListener implements LocationListener {
        @NonNull
        private final LocationListenerHolder locationListenerHolder;
        @NonNull
        private final LocationManager locationManager;
        @NonNull
        private final String provider;

        ProviderAwareLocationListener(@NonNull LocationListenerHolder locationListenerHolder,
                                      @NonNull LocationManager locationManager,
                                      @NonNull String provider) {
            this.locationListenerHolder = locationListenerHolder;
            this.locationManager = locationManager;
            this.provider = provider;
        }

        @NonNull
        String getProvider() {
            return provider;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                for (OnLocationChangeListener listener : locationListenerHolder.subscribers) {
                    listener.onLocationChanged(location);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            locationListenerHolder.updateProviderAvailability(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            locationListenerHolder.updateProviderAvailability(provider);
        }
    }

    public interface OnLocationChangeListener {
        void onLocationChanged(@NonNull Location location);

        void onLocationProvidersAvailabilityChanged(boolean networkProviderEnabled, boolean gpsProviderEnabled);
    }
}
