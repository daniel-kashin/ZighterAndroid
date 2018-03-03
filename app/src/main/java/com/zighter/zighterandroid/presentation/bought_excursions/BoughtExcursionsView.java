package com.zighter.zighterandroid.presentation.bought_excursions;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;

import java.util.List;

interface BoughtExcursionsView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showLoading();

    @StateStrategyType(SingleStateStrategy.class)
    void showExcursions(@NonNull List<BoughtExcursion> excursions);

    @StateStrategyType(SingleStateStrategy.class)
    void showNetworkUnavailable();

    @StateStrategyType(SingleStateStrategy.class)
    void showServerException();
}
