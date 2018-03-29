package com.zighter.zighterandroid.dagger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.zighter.zighterandroid.dagger.component.AccountComponent;
import com.zighter.zighterandroid.dagger.component.AppComponent;
import com.zighter.zighterandroid.dagger.component.BoughtExcursionsComponent;
import com.zighter.zighterandroid.dagger.component.DaggerAppComponent;
import com.zighter.zighterandroid.dagger.component.DownloadExcursionJobComponent;
import com.zighter.zighterandroid.dagger.component.ExcursionMapComponent;
import com.zighter.zighterandroid.dagger.component.GuideComponent;
import com.zighter.zighterandroid.dagger.component.LoginComponent;
import com.zighter.zighterandroid.dagger.component.MediaComponent;
import com.zighter.zighterandroid.dagger.component.SearchComponent;
import com.zighter.zighterandroid.dagger.component.SightComponent;
import com.zighter.zighterandroid.dagger.module.singleton.AppModule;
import com.zighter.zighterandroid.presentation.login.LoginPresenter;

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

    @Nullable
    @UiThread
    public static Injector getInstanceSafe() {
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
    @NonNull
    private WeakReference<BoughtExcursionsComponent> boughtExcursionsComponentWeakReference;
    @NonNull
    private WeakReference<DownloadExcursionJobComponent> downloadExcursionJobComponentWeakReference;
    @NonNull
    private WeakReference<SearchComponent> searchComponentWeakReference;
    @NonNull
    private WeakReference<LoginComponent> loginPresenterWeakReference;
    @NonNull
    private WeakReference<GuideComponent> guideComponentWeakReference;
    @NonNull
    private WeakReference<AccountComponent> accountComponentWeakReference;

    @UiThread
    private Injector(@NonNull Context context) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .build();
        navigationComponentWeakReference = new WeakReference<>(null);
        sightComponentWeakReference = new WeakReference<>(null);
        mediaComponentWeakReference = new WeakReference<>(null);
        boughtExcursionsComponentWeakReference = new WeakReference<>(null);
        downloadExcursionJobComponentWeakReference = new WeakReference<>(null);
        searchComponentWeakReference = new WeakReference<>(null);
        loginPresenterWeakReference = new WeakReference<>(null);
        guideComponentWeakReference = new WeakReference<>(null);
        accountComponentWeakReference = new WeakReference<>(null);
    }

    @NonNull
    @UiThread
    public AppComponent getAppComponent() {
        return appComponent;
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

    @NonNull
    @UiThread
    public BoughtExcursionsComponent getBoughtExcursionsMediaComponent() {
        if (boughtExcursionsComponentWeakReference.get() == null) {
            boughtExcursionsComponentWeakReference = new WeakReference<>(appComponent.plusBoughtExcursionsComponent());
        }
        return boughtExcursionsComponentWeakReference.get();
    }

    @NonNull
    @UiThread
    public DownloadExcursionJobComponent getDowndloadExcursionJobComponent() {
        if (downloadExcursionJobComponentWeakReference.get() == null) {
            downloadExcursionJobComponentWeakReference = new WeakReference<>(appComponent.plusDownloadExcursionJobComponent());
        }
        return downloadExcursionJobComponentWeakReference.get();
    }

    @NonNull
    @UiThread
    public SearchComponent getSearchComponent() {
        if (searchComponentWeakReference.get() == null) {
            searchComponentWeakReference = new WeakReference<>(appComponent.plusSearchComponent());
        }
        return searchComponentWeakReference.get();
    }

    @NonNull
    @UiThread
    public LoginComponent getLoginComponent() {
        if (loginPresenterWeakReference.get() == null) {
            loginPresenterWeakReference = new WeakReference<>(appComponent.plusLoginComponent());
        }
        return loginPresenterWeakReference.get();
    }

    @NonNull
    @UiThread
    public GuideComponent getGuideComponent() {
        if (guideComponentWeakReference.get() == null) {
            guideComponentWeakReference = new WeakReference<>(appComponent.plusGuideComponent());
        }
        return guideComponentWeakReference.get();
    }

    @NonNull
    @UiThread
    public AccountComponent getAccountComponent() {
        if (accountComponentWeakReference.get() == null) {
            accountComponentWeakReference = new WeakReference<>(appComponent.plusAccountComponent());
        }
        return accountComponentWeakReference.get();
    }
}
