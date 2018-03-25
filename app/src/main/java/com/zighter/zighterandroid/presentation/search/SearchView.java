package com.zighter.zighterandroid.presentation.search;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;

import java.util.List;

interface SearchView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showEmptySearch();

    @StateStrategyType(SingleStateStrategy.class)
    void showEmptyExcursions();

    @StateStrategyType(SingleStateStrategy.class)
    void showExcursions(@NonNull List<BoughtExcursionWithStatus> excursions);

    @StateStrategyType(SingleStateStrategy.class)
    void showLoading();

    @StateStrategyType(SingleStateStrategy.class)
    void showUnhandledException();

    @StateStrategyType(SingleStateStrategy.class)
    void showNetworkUnavailable();

    @StateStrategyType(SingleStateStrategy.class)
    void showServerException();

    @StateStrategyType(SingleStateStrategy.class)
    void showNotAuthorizedException();
}
