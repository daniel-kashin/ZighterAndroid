package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.SightModule;
import com.zighter.zighterandroid.dagger.scope.SightScope;
import com.zighter.zighterandroid.presentation.excursion.sight.SightFragment;

import dagger.Subcomponent;

@SightScope
@Subcomponent(modules = {SightModule.class})
public interface SightComponent {
    void inject(SightFragment sightFragment);
}
