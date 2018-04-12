package com.zighter.zighterandroid.presentation.search;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchExcursion;

import java.util.List;

interface SearchView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showEmptySearch();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showExcursionBuyingLoading();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showExcursionBought();

    @StateStrategyType(SingleStateStrategy.class)
    void showEmptyExcursions();

    @StateStrategyType(SingleStateStrategy.class)
    void showExcursions(@NonNull List<ServiceSearchExcursion> excursions);

    @StateStrategyType(SingleStateStrategy.class)
    void showLoading();

    @StateStrategyType(SingleStateStrategy.class)
    void showUnhandledException();

    @StateStrategyType(SingleStateStrategy.class)
    void showNetworkUnavailable();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showAddingNetworkUnavailable();

    @StateStrategyType(SingleStateStrategy.class)
    void showServerException();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showServerAddingException();

    @StateStrategyType(SingleStateStrategy.class)
    void showNotAuthorizedException();
}
