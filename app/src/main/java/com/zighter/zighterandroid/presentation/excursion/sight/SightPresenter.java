package com.zighter.zighterandroid.presentation.excursion.sight;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.bumptech.glide.Glide;
import com.zighter.zighterandroid.data.entities.excursion.ServiceSight;
import com.zighter.zighterandroid.data.entities.excursion.Sight;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Video;
import com.zighter.zighterandroid.util.DistanceHelper;
import com.zighter.zighterandroid.util.ImageViewLoader;

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

        ImageViewLoader imageViewLoader;

        Image firstImage = sight.getFirstImage();
        if (firstImage != null) {
            imageViewLoader = new ImageViewLoader.UrlLoader(firstImage.getUrl(), false);
        } else {
            Video firstVideo = sight.getFirstVideo();
            imageViewLoader = firstVideo != null
                    ? new ImageViewLoader.UrlLoader(firstVideo.getUrl(), true)
                    : null;
        }

        getViewState().showSight(sight, imageViewLoader);
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
