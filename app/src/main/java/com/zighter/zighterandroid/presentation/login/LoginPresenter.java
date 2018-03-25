package com.zighter.zighterandroid.presentation.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.exception.ServerLoginException;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class LoginPresenter extends BasePresenter<LoginView> {
    private static final String TAG = "LoginPresenter";

    @NonNull
    private final ExcursionRepository excursionRepository;

    @Nullable
    private Disposable loginDisposable;

    public LoginPresenter(@NonNull Scheduler worker,
                          @NonNull Scheduler ui,
                          @NonNull ExcursionRepository excursionRepository) {
        super(worker, ui);
        this.excursionRepository = excursionRepository;
    }

    @Override
    public void onDestroy() {
        if (loginDisposable != null) {
            loginDisposable.dispose();
            loginDisposable = null;
        }
        super.onDestroy();
    }

    void onLogin(@NonNull String username, @NonNull String password) {
        if (loginDisposable != null) {
            loginDisposable.dispose();
            loginDisposable = null;
        }

        loginDisposable = excursionRepository.login(username, password)
                .compose(applySchedulersSingle())
                .subscribe(token -> {
                    getViewState().openBottomNavigation();
                }, throwable -> {
                    if (throwable instanceof BaseException) {
                        if (throwable instanceof NetworkUnavailableException) {
                            getViewState().showNetworkUnavailable();
                        } else if (throwable instanceof ServerException) {
                            if (throwable instanceof ServerLoginException) {
                                getViewState().showServerLoginException();
                            } else {
                                getViewState().showServerException();
                            }
                        }
                    } else {
                        getViewState().showUnhandledException();
                        handleThrowable(throwable, TAG);
                    }
                });
    }
}
