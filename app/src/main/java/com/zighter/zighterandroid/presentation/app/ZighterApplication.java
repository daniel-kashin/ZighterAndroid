package com.zighter.zighterandroid.presentation.app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class ZighterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) return;
        LeakCanary.install(this);
    }

}
