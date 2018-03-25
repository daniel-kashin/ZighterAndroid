package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.GuideModule;
import com.zighter.zighterandroid.dagger.scope.GuideScope;
import com.zighter.zighterandroid.presentation.guide.GuideActivity;

import dagger.Subcomponent;

@Subcomponent(modules = GuideModule.class)
@GuideScope
public interface GuideComponent {
    void inject(GuideActivity guideActivity);
}
