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
    private static final long MIN_GPS_LOCATION_REQUEST_TIME = TimeUnit.SECONDS.toMillis(20);
    private static final long MIN_NETWORK_LOCATION_REQUEST_TIME = TimeUnit.SECONDS.toMillis(5);
    private static final long MIN_LOCATION_REQUEST_DISTANCE_IN_METERS = 20;

    @NonNull
    private final LocationManager locationManager;
    @NonNull
    private final List<OnLocationChangeListener> subscribers;
    @NonNull
    private final ProviderAwareLocationListener gpsLocationListener;
    @NonNull
    private final ProviderAwareLocationListener networkLocationListener;

    private boolean areLocationUpdatesActive;
    @Nullable
    private Boolean isGpsLocationEnabled;
    @Nullable
    private Boolean isNetworkLocationEnabled;
    @Nullable
    private Location lastKnownLocation;

    @UiThread
    public LocationListenerHolder(@NonNull LocationManager locationManager) {
        this.locationManager = locationManager;
        subscribers = new ArrayList<>();
        gpsLocationListener = new ProviderAwareLocationListener(this, GPS_PROVIDER);
        networkLocationListener = new ProviderAwareLocationListener(this, NETWORK_PROVIDER);
    }

    @UiThread
    public void register(@NonNull OnLocationChangeListener subscriber) {
        if (!isRegistered(subscriber)) {
            boolean wasEmpty = subscribers.isEmpty();
            subscribers.add(subscriber);
            if (wasEmpty) updateProvidersAvailability();
            notifyListener(subscriber);
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

    private boolean isRegistered(@NonNull OnLocationChangeListener subscriber) {
        return subscribers.contains(subscriber);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        if (!areLocationUpdatesActive) {
            areLocationUpdatesActive = true;
            locationManager.requestLocationUpdates(gpsLocationListener.getProvider(),
                                                   MIN_GPS_LOCATION_REQUEST_TIME,
                                                   MIN_LOCATION_REQUEST_DISTANCE_IN_METERS,
                                                   gpsLocationListener);
            locationManager.requestLocationUpdates(networkLocationListener.getProvider(),
                                                   MIN_NETWORK_LOCATION_REQUEST_TIME,
                                                   MIN_LOCATION_REQUEST_DISTANCE_IN_METERS,
                                                   networkLocationListener);
        }
    }

    private void stopLocationUpdates() {
        if (areLocationUpdatesActive) {
            areLocationUpdatesActive = false;
            stopLocationUpdates(gpsLocationListener);
            stopLocationUpdates(networkLocationListener);
        }
    }

    private void stopLocationUpdates(@NonNull ProviderAwareLocationListener listener) {
        locationManager.removeUpdates(listener);
    }

    @NonNull
    private ProvidersUpdateResult updateProvidersAvailability() {
        LocationState locationStatePrevious = getLocationState();
        Boolean isGpsLocationEnabledPrevious = isGpsLocationEnabled;
        Boolean isNetworkLocationEnabledPrevious = isNetworkLocationEnabled;

        isGpsLocationEnabled = LocationSettingsHelper.isGpsLocationEnabled(locationManager);
        isNetworkLocationEnabled = LocationSettingsHelper.isNetworkLocationEnabled(locationManager);

        boolean providersUpdated = isGpsLocationEnabledPrevious == null
                || isNetworkLocationEnabledPrevious == null
                || isGpsLocationEnabledPrevious != isGpsLocationEnabled
                || isNetworkLocationEnabledPrevious != isNetworkLocationEnabled;

        LocationState locationState = getLocationState();
        boolean locationStateUpdated = locationState != locationStatePrevious
                && locationState != LocationState.ACTIVE;

        return new ProvidersUpdateResult(providersUpdated, locationStateUpdated);
    }

    private void notifyListeners(@NonNull ProvidersUpdateResult providersUpdateResult) {
        if (isNetworkLocationEnabled == null || isGpsLocationEnabled == null) {
            throw new IllegalStateException();
        }

        if (!subscribers.isEmpty() && providersUpdateResult.getProvidersUpdated()) {
            for (OnLocationChangeListener listener : subscribers) {
                listener.onLocationProvidersAvailabilityChanged(isNetworkLocationEnabled, isGpsLocationEnabled);

                if (providersUpdateResult.getLocationStateUpdated()) {
                    if (lastKnownLocation != null) {
                        listener.onLocationChanged(lastKnownLocation,
                                                   getLocationState() == LocationState.ACTIVE);
                    }
                }
            }
        }
    }

    private void notifyListener(@NonNull OnLocationChangeListener listenerToForceNotify) {
        if (isNetworkLocationEnabled == null || isGpsLocationEnabled == null) {
            throw new IllegalStateException();
        }

        listenerToForceNotify.onLocationProvidersAvailabilityChanged(isNetworkLocationEnabled, isGpsLocationEnabled);

        lastKnownLocation = LocationSettingsHelper.getLastKnownLocation(locationManager, lastKnownLocation);
        if (lastKnownLocation != null) {
            listenerToForceNotify.onLocationChanged(lastKnownLocation,
                                                    getLocationState() == LocationState.ACTIVE);
        }
    }

    private LocationState getLocationState() {
        if (isGpsLocationEnabled == null && isNetworkLocationEnabled == null) {
            return LocationState.NOT_INITIALIZED;
        } else if (isGpsLocationEnabled != null && isGpsLocationEnabled
                || isNetworkLocationEnabled != null && isNetworkLocationEnabled) {
            return LocationState.ACTIVE;
        } else {
            return LocationState.OUTDATED;
        }
    }

    private static class ProviderAwareLocationListener implements LocationListener {
        @NonNull
        private final LocationListenerHolder locationListenerHolder;
        @NonNull
        private final String provider;

        ProviderAwareLocationListener(@NonNull LocationListenerHolder locationListenerHolder,
                                      @NonNull String provider) {
            this.locationListenerHolder = locationListenerHolder;
            this.provider = provider;
        }

        @NonNull
        String getProvider() {
            return provider;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                locationListenerHolder.lastKnownLocation = location;
                for (OnLocationChangeListener listener : locationListenerHolder.subscribers) {
                    listener.onLocationChanged(location, true);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
            ProvidersUpdateResult providersUpdateResult = locationListenerHolder.updateProvidersAvailability();
            locationListenerHolder.notifyListeners(providersUpdateResult);
        }

        @Override
        public void onProviderDisabled(String provider) {
            ProvidersUpdateResult providersUpdateResult = locationListenerHolder.updateProvidersAvailability();
            locationListenerHolder.notifyListeners(providersUpdateResult);
        }
    }

    public interface OnLocationChangeListener {
        void onLocationChanged(@NonNull Location location, boolean isActive);

        void onLocationProvidersAvailabilityChanged(boolean networkProviderEnabled, boolean gpsProviderEnabled);
    }

    private enum LocationState {
        ACTIVE,
        OUTDATED,
        NOT_INITIALIZED
    }
}
