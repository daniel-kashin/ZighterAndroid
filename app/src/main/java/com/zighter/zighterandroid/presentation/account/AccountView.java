package com.zighter.zighterandroid.presentation.account;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SingleStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.zighter.zighterandroid.data.entities.presentation.Guide;

interface AccountView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showInfo(@NonNull String login);

    @StateStrategyType(SingleStateStrategy.class)
    void showNotAuthorizedException();
}
