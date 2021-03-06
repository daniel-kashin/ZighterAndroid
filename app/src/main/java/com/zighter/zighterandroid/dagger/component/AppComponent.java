package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.singleton.AppModule;
import com.zighter.zighterandroid.dagger.module.singleton.DatabaseModule;
import com.zighter.zighterandroid.dagger.module.singleton.DownloadExcursionModule;
import com.zighter.zighterandroid.dagger.module.singleton.LocationModule;
import com.zighter.zighterandroid.dagger.module.singleton.NetworkModule;
import com.zighter.zighterandroid.dagger.module.singleton.ExcursionModule;
import com.zighter.zighterandroid.dagger.module.singleton.SchedulerModule;
import com.zighter.zighterandroid.dagger.module.singleton.SystemManagersModule;
import com.zighter.zighterandroid.dagger.module.singleton.TokenModule;
import com.zighter.zighterandroid.presentation.splash.SplashActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class,
        ExcursionModule.class,
        SchedulerModule.class,
        SystemManagersModule.class,
        LocationModule.class,
        DownloadExcursionModule.class,
        DatabaseModule.class,
        TokenModule.class})
public interface AppComponent {

    ExcursionMapComponent plusNavigationComponent();

    SightComponent plusSightComponent();

    MediaComponent plusMediaComponent();

    BoughtExcursionsComponent plusBoughtExcursionsComponent();

    DownloadExcursionJobComponent plusDownloadExcursionJobComponent();

    SearchComponent plusSearchComponent();

    LoginComponent plusLoginComponent();

    GuideComponent plusGuideComponent();

    AccountComponent plusAccountComponent();

    RegistrationComponent plusRegistrationComponent();

    void inject(SplashActivity splashActivity);
}
