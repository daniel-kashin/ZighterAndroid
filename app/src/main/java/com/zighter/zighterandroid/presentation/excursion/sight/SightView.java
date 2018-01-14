package com.zighter.zighterandroid.presentation.excursion.sight;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.service.Sight;

public interface SightView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showSight(@NonNull Sight sight);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showCurrentDistance(int distanceInMeters);

    @StateStrategyType(SkipStrategy.class)
    void ensureLocationPermissionGranted();
}
