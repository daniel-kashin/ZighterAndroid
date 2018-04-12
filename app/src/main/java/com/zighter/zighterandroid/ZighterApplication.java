package com.zighter.zighterandroid;

import android.app.Application;

import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crash.FirebaseCrash;
import com.mapbox.mapboxsdk.Mapbox;
import com.squareup.leakcanary.LeakCanary;
import com.zighter.zighterandroid.dagger.Injector;

public class ZighterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // mapbox
        Mapbox.getInstance(getApplicationContext(), BuildConfig.MAPBOX_API_KEY);

        // injector
        Injector.initialize(this);

        // firebase
        FirebaseApp.initializeApp(this);

        // stetho
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                                  .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                                  .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                                  .build());
    }

}
