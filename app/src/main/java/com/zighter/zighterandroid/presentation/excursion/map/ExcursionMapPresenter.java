package com.zighter.zighterandroid.presentation.excursion.map;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.repositories.excursions.ExcursionsRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;

@SuppressWarnings("CodeBlock2Expr")
@InjectViewState
public class ExcursionMapPresenter extends BasePresenter<ExcursionMapView> {

    private static final String TAG = "[ExcursionMapPresenter]";
    private boolean isLocationPermissionGranted;
    private boolean isNetworkLocationEnabled;
    private boolean isGpsLocationEnabled;

    @NonNull
    private final ExcursionsRepository excursionsRepository;

    public ExcursionMapPresenter(@NonNull ExcursionsRepository excursionsRepository,
                                 @NonNull Scheduler worker,
                                 @NonNull Scheduler ui) {
        super(worker, ui);
        this.excursionsRepository = excursionsRepository;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showLoading();

        excursionsRepository.getExcursion()
                .compose(applySchedulersSingle())
                .subscribe(excursion -> {
                    getViewState().showExcursion(excursion);
                }, throwable -> {
                   if (throwable instanceof BaseException) {
                       if (throwable instanceof NetworkUnavailableException) {
                           getViewState().showNetworkUnavailable();
                       } else if (throwable instanceof ServerException) {
                           getViewState().showServerException();
                       }
                   } else {
                        handleThrowable(throwable, TAG);
                   }
                });
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

    void onLocationChanged(@NonNull Location location, boolean active) {
        getViewState().showCurrentLocation(location, active);
    }

    void onSightClicked(@Nullable Sight sight, @Nullable Marker marker) {
        getViewState().showSightSelection(sight, marker);
    }
}
