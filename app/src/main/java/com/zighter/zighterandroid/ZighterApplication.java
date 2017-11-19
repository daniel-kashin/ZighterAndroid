package com.zighter.zighterandroid;

import android.app.Application;

import com.mapbox.mapboxsdk.Mapbox;
import com.squareup.leakcanary.LeakCanary;
import com.zighter.zighterandroid.dagger.Injector;

public class ZighterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // leak canary
        if (LeakCanary.isInAnalyzerProcess(this)) return;
        LeakCanary.install(this);

        // mapbox
        Mapbox.getInstance(getApplicationContext(), BuildConfig.MAPBOX_API_KEY);

        // injector
        Injector.initialize(this);
    }

}
