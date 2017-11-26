package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.NavigationModule;
import com.zighter.zighterandroid.dagger.scope.NavigationScope;
import com.zighter.zighterandroid.presentation.excursion.map.ExcursionMapFragment;

import dagger.Subcomponent;

@NavigationScope
@Subcomponent(modules = {NavigationModule.class})
public interface NavigationComponent {

    void inject(ExcursionMapFragment excursionMapFragment);

}
