package com.zighter.zighterandroid.presentation.login;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

interface LoginView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showNetworkUnavailable();

    @StateStrategyType(SingleStateStrategy.class)
    void showServerException();

    @StateStrategyType(SingleStateStrategy.class)
    void showUnhandledException();

    @StateStrategyType(SingleStateStrategy.class)
    void showServerLoginException();

    @StateStrategyType(SingleStateStrategy.class)
    void openBottomNavigation();
}
