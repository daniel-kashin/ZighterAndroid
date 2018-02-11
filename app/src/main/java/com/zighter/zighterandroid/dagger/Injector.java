package com.zighter.zighterandroid.dagger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.zighter.zighterandroid.dagger.component.AppComponent;
import com.zighter.zighterandroid.dagger.component.DaggerAppComponent;
import com.zighter.zighterandroid.dagger.component.ExcursionMapComponent;
import com.zighter.zighterandroid.dagger.component.MediaComponent;
import com.zighter.zighterandroid.dagger.component.SightComponent;
import com.zighter.zighterandroid.dagger.module.singleton.AppModule;

import java.lang.ref.WeakReference;

public class Injector {
    @Nullable
    private static Injector instance;

    @UiThread
    public static void initialize(final Context context) {
        instance = new Injector(context);
    }

    @NonNull
    @UiThread
    public static Injector getInstance() {
        if (instance == null) {
            throw new IllegalStateException();
        }
        return instance;
    }

    @NonNull
    private final AppComponent appComponent;
    @NonNull
    private WeakReference<ExcursionMapComponent> navigationComponentWeakReference;
    @NonNull
    private WeakReference<SightComponent> sightComponentWeakReference;
    @NonNull
    private WeakReference<MediaComponent> mediaComponentWeakReference;

    @UiThread
    private Injector(@NonNull Context context) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
        navigationComponentWeakReference = new WeakReference<>(null);
        sightComponentWeakReference = new WeakReference<>(null);
        mediaComponentWeakReference = new WeakReference<>(null);
    }

    @NonNull
    @UiThread
    public ExcursionMapComponent getNavigationComponent() {
        if (navigationComponentWeakReference.get() == null) {
            navigationComponentWeakReference = new WeakReference<>(appComponent.plusNavigationComponent());
        }
        return navigationComponentWeakReference.get();
    }

    @NonNull
    @UiThread
    public SightComponent getSightComponent() {
        if (sightComponentWeakReference.get() == null) {
            sightComponentWeakReference = new WeakReference<>(appComponent.plusSightComponent());
        }
        return sightComponentWeakReference.get();
    }

    @NonNull
    @UiThread
    public MediaComponent getMediaComponent() {
        if (mediaComponentWeakReference.get() == null) {
            mediaComponentWeakReference = new WeakReference<>(appComponent.plusMediaComponent());
        }
        return mediaComponentWeakReference.get();
    }
}
