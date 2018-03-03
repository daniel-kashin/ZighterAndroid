package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.BoughtExcursionsModule;
import com.zighter.zighterandroid.dagger.scope.BoughtExcursionsScope;
import com.zighter.zighterandroid.presentation.bought_excursions.BoughtExcursionsFragment;

import dagger.Subcomponent;

@BoughtExcursionsScope
@Subcomponent(modules = BoughtExcursionsModule.class)
public interface BoughtExcursionsComponent {
    void inject(BoughtExcursionsFragment boughtExcursionsFragment);
}
