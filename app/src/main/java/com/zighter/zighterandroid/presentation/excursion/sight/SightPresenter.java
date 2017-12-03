package com.zighter.zighterandroid.presentation.excursion.sight;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.util.LocationHelper;

import java.util.concurrent.TimeUnit;

@InjectViewState
public class SightPresenter extends MvpPresenter<SightView> {

    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final long MIN_LOCATION_REQUEST_TIME = TimeUnit.SECONDS.toMillis(1);
    private static final long MIN_LOCATION_REQUEST_DISTANCE_IN_METERS = 1;

    @NonNull
    private final Sight sight;
    @NonNull
    private final LocationManager locationManager;
    @NonNull
    private final Context applicationContext;

    SightPresenter(@NonNull Sight sight,
                   @NonNull LocationManager locationManager,
                   @NonNull Context applicationContext) {
        this.sight = sight;
        this.locationManager = locationManager;
        this.applicationContext = applicationContext;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showSight(sight);
        getViewState().ensureLocationEnabled();
    }

    void onLocationPermissionEnabled() {
        int currentLocationPermission = ContextCompat.checkSelfPermission(applicationContext, LOCATION_PERMISSION);
        if (currentLocationPermission != PackageManager.PERMISSION_GRANTED) return;

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        handleLocationChange(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                MIN_LOCATION_REQUEST_TIME,
                MIN_LOCATION_REQUEST_DISTANCE_IN_METERS,
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        handleLocationChange(location);
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
                });
    }

    private void handleLocationChange(@Nullable Location location) {
        if (location != null) {
            int distanceInMeters = (int) LocationHelper.distanceInMeters(
                    location.getLatitude(),
                    sight.getLatitude(),
                    location.getLongitude(),
                    sight.getLongitude());
            getViewState().showCurrentDistance(distanceInMeters);
        }
    }

    public static class Builder {

        @NonNull
        private final LocationManager locationManager;
        @NonNull
        private final Context applicationContext;
        @Nullable
        private Sight sight;

        public Builder(@NonNull LocationManager locationManager, @NonNull Context applicationContext) {
            this.locationManager = locationManager;
            this.applicationContext = applicationContext;
        }

        public Builder setSight(@NonNull Sight sight) {
            this.sight = sight;
            return this;
        }

        public SightPresenter build() {
            if (sight == null)
                throw new IllegalStateException("Sight must be defined for creating SightPresenter");
            return new SightPresenter(sight, locationManager, applicationContext);
        }

    }
}