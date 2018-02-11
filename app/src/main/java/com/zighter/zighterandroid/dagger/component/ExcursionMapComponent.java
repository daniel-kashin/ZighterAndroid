package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.ExcursionMapModule;
import com.zighter.zighterandroid.dagger.scope.ExcursionMapScope;
import com.zighter.zighterandroid.presentation.excursion.map.ExcursionMapFragment;

import dagger.Subcomponent;

@ExcursionMapScope
@Subcomponent(modules = {ExcursionMapModule.class})
public interface ExcursionMapComponent {
    void inject(ExcursionMapFragment excursionMapFragment);
}
