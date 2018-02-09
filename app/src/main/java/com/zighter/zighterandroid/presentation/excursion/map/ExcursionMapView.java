package com.zighter.zighterandroid.presentation.excursion.map;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.zighter.zighterandroid.data.entities.excursion.Excursion;
import com.zighter.zighterandroid.data.entities.excursion.ServiceSight;
import com.zighter.zighterandroid.data.entities.excursion.Sight;

interface ExcursionMapView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showLoading();

    @StateStrategyType(SingleStateStrategy.class)
    void showExcursion(@NonNull Excursion excursion);

    @StateStrategyType(SingleStateStrategy.class)
    void showNetworkUnavailable();

    @StateStrategyType(SingleStateStrategy.class)
    void showServerException();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSightSelection(@Nullable Sight sight, @Nullable Marker marker);

    @StateStrategyType(SkipStrategy.class)
    void showCurrentLocation(@NonNull Location location, boolean active);

    @StateStrategyType(SkipStrategy.class)
    void updateLocationAvailability(boolean isPermissionGranted, boolean isLocationProviderEnabled);
}
