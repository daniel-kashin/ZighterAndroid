package com.zighter.zighterandroid.presentation.excursion.map;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;

@SuppressWarnings("Convert2MethodRef")
@InjectViewState
public class ExcursionMapPresenter extends BasePresenter<ExcursionMapView> {

    private static final String TAG = "[ExcursionMapPresenter]";
    private boolean isLocationPermissionGranted;
    private boolean isNetworkLocationEnabled;
    private boolean isGpsLocationEnabled;

    @NonNull
    private final ExcursionRepository excursionRepository;
    @NonNull
    private final String excursionUuid;

    public ExcursionMapPresenter(@NonNull ExcursionRepository excursionRepository,
                                 @NonNull Scheduler worker,
                                 @NonNull Scheduler ui,
                                 @NonNull String excursionUuid) {
        super(worker, ui);
        this.excursionRepository = excursionRepository;
        this.excursionUuid = excursionUuid;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        onReloadExcursionRequest();
    }

    void onReloadExcursionRequest() {
        excursionRepository.getExcursion(excursionUuid)
                .compose(applySchedulersSingle())
                .doOnSubscribe(disposable -> getViewState().showLoading())
                .subscribe(excursion -> {
                    getViewState().showExcursion(excursion);
                }, throwable -> {
                    handleThrowable(throwable);
                });
    }

    private void handleThrowable(Throwable throwable) {
        if (throwable instanceof BaseException) {
            if (throwable instanceof NetworkUnavailableException) {
                getViewState().showNetworkUnavailable();
            } else if (throwable instanceof ServerException) {
                if (throwable instanceof ServerNotAuthorizedException) {
                    getViewState().showNotAuthorizedException();
                } else {
                    getViewState().showServerException();
                }
            }
        } else {
            getViewState().showUnhandledException();
            handleThrowable(throwable, TAG);
        }
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

    void onSightClicked(@Nullable Sight sight) {
        getViewState().showSightSelection(sight);
    }

    public static class Builder {
        @NonNull
        private final ExcursionRepository excursionRepository;
        @NonNull
        private final Scheduler worker;
        @NonNull
        private final Scheduler ui;
        @Nullable
        private String excursionUuid;

        public Builder(@NonNull ExcursionRepository excursionRepository,
                       @NonNull Scheduler worker,
                       @NonNull Scheduler ui) {
            this.excursionRepository = excursionRepository;
            this.worker = worker;
            this.ui = ui;
        }

        @NonNull
        public Builder excursionUuid(@NonNull String excursionUuid) {
            this.excursionUuid = excursionUuid;
            return this;
        }

        @NonNull
        public ExcursionMapPresenter build() {
            if (excursionUuid == null) {
                throw new IllegalStateException();
            }
            return new ExcursionMapPresenter(excursionRepository, worker, ui, excursionUuid);
        }

    }
}
