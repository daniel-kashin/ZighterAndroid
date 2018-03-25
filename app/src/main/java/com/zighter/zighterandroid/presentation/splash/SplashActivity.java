package com.zighter.zighterandroid.presentation.splash;

import android.os.Bundle;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.repositories.token.TokenStorage;
import com.zighter.zighterandroid.presentation.bottom_navigation.BottomNavigationActivity;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
import com.zighter.zighterandroid.presentation.login.LoginActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseSupportActivity {
    @Inject
    TokenStorage tokenStorage;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getAppComponent()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (tokenStorage.isTokenPresent()) {
            BottomNavigationActivity.start(this);
        } else {
            LoginActivity.start(this);
        }
    }
}
