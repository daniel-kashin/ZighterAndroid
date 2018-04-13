package com.zighter.zighterandroid;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crash.FirebaseCrash;
import com.mapbox.mapboxsdk.Mapbox;
import com.zighter.zighterandroid.dagger.Injector;

public class ZighterApplication extends Application {
    private static final String TAG = "ZighterApplication";

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

        // catch all exceptions
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> handleUncaughtException(e));
    }

    public void handleUncaughtException(@Nullable Throwable throwable) {
        if (throwable == null) {
            return;
        }

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            handleUncaughtExceptionInner(throwable);
        } else {
            new Handler(Looper.getMainLooper()).post(() -> handleUncaughtExceptionInner(throwable));
        }
    }

    private void handleUncaughtExceptionInner(@NonNull Throwable throwable) {
        FirebaseCrash.log(TAG);
        FirebaseCrash.report(throwable);
        System.exit(1);
    }
}
