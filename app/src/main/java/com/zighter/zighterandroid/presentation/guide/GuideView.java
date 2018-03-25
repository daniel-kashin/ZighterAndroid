package com.zighter.zighterandroid.presentation.guide;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.presentation.Guide;

import java.util.List;

interface GuideView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showGuide(@NonNull Guide guide);

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
