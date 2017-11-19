package com.zighter.zighterandroid.dagger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.zighter.zighterandroid.dagger.component.AppComponent;
import com.zighter.zighterandroid.dagger.component.DaggerAppComponent;
import com.zighter.zighterandroid.dagger.component.NavigationComponent;
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
            throw new IllegalStateException("Injector must be initialized before getting");
        }
        return instance;
    }


    @NonNull
    private final AppComponent appComponent;
    @NonNull
    private WeakReference<NavigationComponent> navigationComponentWeakReference;

    private Injector(@NonNull Context context) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
        navigationComponentWeakReference = new WeakReference<>(null);
    }

    @NonNull
    public NavigationComponent getNavigationComponent() {
        if (navigationComponentWeakReference.get() == null) {
            navigationComponentWeakReference = new WeakReference<>(appComponent.plusNavigationComponent());
        }
        return navigationComponentWeakReference.get();
    }



}
