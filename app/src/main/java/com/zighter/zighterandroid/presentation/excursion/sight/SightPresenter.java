package com.zighter.zighterandroid.presentation.excursion.sight;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.data.location.LocationListenerHolder;
import com.zighter.zighterandroid.util.DistanceHelper;

import static com.zighter.zighterandroid.data.location.LocationListenerHolder.OnLocationChangeListener;

@InjectViewState
public class SightPresenter extends MvpPresenter<SightView> {
    @NonNull
    private final Sight sight;

    private boolean isLocationPermissionGranted;
    private boolean isNetworkLocationEnabled;
    private boolean isGpsLocationEnabled;

    SightPresenter(@NonNull Sight sight) {
        this.sight = sight;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showSight(sight);
    }

    void onLocationChanged(@NonNull Location location, boolean active) {
        Integer distanceInMeters = active ?
                (int) DistanceHelper.distanceInMeters(location.getLatitude(), sight.getLatitude(),
                                                      location.getLongitude(), sight.getLongitude())
                : null;
        getViewState().showCurrentDistance(distanceInMeters);
    }

    void onLocationProvidersAvailabilityChanged(boolean networkProviderEnabled, boolean gpsProviderEnabled) {
        isNetworkLocationEnabled = networkProviderEnabled;
        isGpsLocationEnabled = gpsProviderEnabled;
        boolean isLocationProviderEnabled = isNetworkLocationEnabled || isGpsLocationEnabled;
        getViewState().updateLocationAvailability(isLocationPermissionGranted, isLocationProviderEnabled);
    }

    void onLocationPermissionGranted(boolean granted) {
        isLocationPermissionGranted = granted;
        boolean isLocationProviderEnabled = isNetworkLocationEnabled || isGpsLocationEnabled;
        getViewState().updateLocationAvailability(isLocationPermissionGranted, isLocationProviderEnabled);
    }

    public static class Builder {
        @Nullable
        private Sight sight;

        public Builder() {
        }

        public Builder setSight(@NonNull Sight sight) {
            this.sight = sight;
            return this;
        }

        public SightPresenter build() {
            if (sight == null) {
                throw new IllegalStateException("Sight must be defined for creating SightPresenter");
            }
            return new SightPresenter(sight);
        }
    }
}
