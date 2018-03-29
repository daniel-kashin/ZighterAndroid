package com.zighter.zighterandroid.presentation.account;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class AccountPresenter extends BasePresenter<AccountView> {
    @NonNull
    private final ExcursionRepository excursionRepository;
    @Nullable
    private Disposable getLoginDisposable;
    @Nullable
    private Disposable logOutDisposable;

    public AccountPresenter(@NonNull ExcursionRepository excursionRepository,
                            @NonNull Scheduler worker,
                            @NonNull Scheduler ui) {
        super(worker, ui);
        this.excursionRepository = excursionRepository;
    }

    @Override
    public void onDestroy() {
        if (getLoginDisposable != null) {
            getLoginDisposable.dispose();
            getLoginDisposable = null;
        }
        if (logOutDisposable != null) {
            logOutDisposable.dispose();
            logOutDisposable = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        if (getLoginDisposable != null) {
            getLoginDisposable.dispose();
            getLoginDisposable = null;
        }

        getLoginDisposable = excursionRepository.getLogin()
                .compose(applySchedulersSingle())
                .subscribe(login -> {
                    getViewState().showInfo(login);
                }, throwable -> {
                    getViewState().showNotAuthorizedException();
                });
    }

    public void onLogOutClicked() {
        if (logOutDisposable != null) {
            logOutDisposable.dispose();
            logOutDisposable = null;
        }

        logOutDisposable = excursionRepository.logOut()
                .compose(applySchedulersCompletable())
                .subscribe(() -> {
                    getViewState().showNotAuthorizedException();
                }, throwable -> {
                    // do nothing
                });
    }
}
