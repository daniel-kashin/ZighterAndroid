package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    NavigationComponent plusNavigationComponent();

}
