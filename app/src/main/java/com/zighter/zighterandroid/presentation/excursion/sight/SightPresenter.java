package com.zighter.zighterandroid.presentation.excursion.sight;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.util.LocationListenerHolder;
import com.zighter.zighterandroid.util.LocationHelper;

import static com.zighter.zighterandroid.util.LocationListenerHolder.OnLocationChangeListener;

@InjectViewState
public class SightPresenter extends MvpPresenter<SightView> implements OnLocationChangeListener {
    @NonNull
    private final Sight sight;
    @NonNull
    private final LocationManager locationManager;
    @NonNull
    private final LocationListenerHolder locationListenerHolder;

    SightPresenter(@NonNull Sight sight,
                   @NonNull LocationManager locationManager) {
        this.sight = sight;
        this.locationManager = locationManager;
        this.locationListenerHolder = new LocationListenerHolder(this, locationManager);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showSight(sight);
        getViewState().ensureLocationPermissionEnabled();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationListenerHolder.stopLocationUpdates();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        int distanceInMeters = (int) LocationHelper.distanceInMeters(location.getLatitude(),
                                                                     sight.getLatitude(),
                                                                     location.getLongitude(),
                                                                     sight.getLongitude());
        getViewState().showCurrentDistance(distanceInMeters);
    }

    void onLocationPermissionEnabled() {
        locationListenerHolder.startLocationUpdates();
    }

    public static class Builder {
        @NonNull
        private final LocationManager locationManager;
        @Nullable
        private Sight sight;

        public Builder(@NonNull LocationManager locationManager) {
            this.locationManager = locationManager;
        }

        public Builder setSight(@NonNull Sight sight) {
            this.sight = sight;
            return this;
        }

        public SightPresenter build() {
            if (sight == null) {
                throw new IllegalStateException("Sight must be defined for creating SightPresenter");
            }
            return new SightPresenter(sight, locationManager);
        }
    }
}
