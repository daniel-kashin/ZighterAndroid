package com.zighter.zighterandroid.dagger.module;

import com.zighter.zighterandroid.dagger.scope.NavigationScope;
import com.zighter.zighterandroid.presentation.map.NavigationPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigationModule {

    @NavigationScope
    @Provides
    NavigationPresenter provideNavigationPresenter() {
        return new NavigationPresenter();
    }

}
