package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.LoginModule;
import com.zighter.zighterandroid.dagger.scope.LoginScope;
import com.zighter.zighterandroid.presentation.login.LoginActivity;

import dagger.Subcomponent;

@LoginScope
@Subcomponent(modules = {LoginModule.class})
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
