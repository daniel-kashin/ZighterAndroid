package com.zighter.zighterandroid.presentation.excursion.map;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.zighter.zighterandroid.data.entities.service.Excursion;
import com.zighter.zighterandroid.data.entities.service.Sight;

interface ExcursionMapView extends MvpView {

    @StateStrategyType(SingleStateStrategy.class)
    void handleLoadingShowing();

    @StateStrategyType(SingleStateStrategy.class)
    void handleExcursionShowing(@NonNull Excursion excursion);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void handleSightSelection(@NonNull Sight sight, @NonNull Marker marker);
}
