package com.zighter.zighterandroid.presentation.bought_excursions;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;

import java.util.List;
import static com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus.DownloadStatus;

interface BoughtExcursionsView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showLoading();

    @StateStrategyType(SingleStateStrategy.class)
    void showExcursions(@NonNull List<BoughtExcursionWithStatus> excursions);

    @StateStrategyType(AddToEndStrategy.class)
    void showExcursionStatus(@NonNull String boughtExcursionUuid, @NonNull DownloadStatus downloadStatus);

    @StateStrategyType(SingleStateStrategy.class)
    void showNetworkUnavailable();

    @StateStrategyType(SingleStateStrategy.class)
    void showServerException();

    @StateStrategyType(SingleStateStrategy.class)
    void showEmptyExcursions();
}
