package com.zighter.zighterandroid.presentation.excursion.sight;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.excursion.ServiceSight;
import com.zighter.zighterandroid.data.entities.excursion.Sight;

public interface SightView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showSight(@NonNull Sight sight);

    @StateStrategyType(SkipStrategy.class)
    void showCurrentDistance(Integer distanceInMeters);

    @StateStrategyType(SkipStrategy.class)
    void updateLocationAvailability(boolean isPermissionGranted, boolean isLocationProviderEnabled);
}
