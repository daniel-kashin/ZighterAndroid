package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.singleton.AppModule;
import com.zighter.zighterandroid.dagger.module.singleton.NetworkModule;
import com.zighter.zighterandroid.dagger.module.singleton.PathsModule;
import com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, PathsModule.class, SchedulerModule.class})
public interface AppComponent {

    NavigationComponent plusNavigationComponent();

}
