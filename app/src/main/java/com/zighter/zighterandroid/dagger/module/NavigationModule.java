package com.zighter.zighterandroid.dagger.module;

import com.zighter.zighterandroid.dagger.scope.NavigationScope;
import com.zighter.zighterandroid.data.paths.PathsRepository;
import com.zighter.zighterandroid.presentation.navigation.NavigationPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class NavigationModule {

    @NavigationScope
    @Provides
    NavigationPresenter provideNavigationPresenter(PathsRepository pathsRepository) {
        return new NavigationPresenter(pathsRepository);
    }

}
