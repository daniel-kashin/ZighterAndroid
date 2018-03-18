package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.MediaModule;
import com.zighter.zighterandroid.dagger.module.SearchModule;
import com.zighter.zighterandroid.dagger.scope.MediaScope;
import com.zighter.zighterandroid.presentation.search.SearchFragment;

import dagger.Subcomponent;

@MediaScope
@Subcomponent(modules = {SearchModule.class})
public interface SearchComponent {
    void inject(SearchFragment searchFragment);
}
