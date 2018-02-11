package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.MediaModule;
import com.zighter.zighterandroid.dagger.scope.MediaScope;
import com.zighter.zighterandroid.presentation.media.MediaActivity;

import dagger.Component;
import dagger.Subcomponent;

@MediaScope
@Subcomponent(modules = {MediaModule.class})
public interface MediaComponent {
    void inject(MediaActivity mediaActivity);
}
