package com.zighter.zighterandroid.presentation.search;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import java.util.ArrayList;

import javax.annotation.Nullable;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class SearchPresenter extends BasePresenter<SearchView> {
    private final String TAG = "SearchPresenter";

    @NonNull
    private final ExcursionRepository excursionRepository;
    @Nullable
    private Disposable getExcursionsDisposable;

    public SearchPresenter(@NonNull ExcursionRepository excursionRepository,
                           @NonNull Scheduler worker,
                           @NonNull Scheduler ui) {
        super(worker, ui);
        this.excursionRepository = excursionRepository;
    }

    @Override
    public void onDestroy() {
        if (getExcursionsDisposable != null) {
            getExcursionsDisposable.dispose();
            getExcursionsDisposable = null;
        }
        super.onDestroy();
    }

    void onSearchTyped(@NonNull String text) {
        if (getExcursionsDisposable != null) {
            getExcursionsDisposable.dispose();
        }

        if (text.isEmpty()) {
            getViewState().showEmptySearch();
        } else {
            getExcursionsDisposable = excursionRepository
                    .searchExcursions(text)
                    .compose(applySchedulersSingle())
                    .doOnSubscribe(disposable -> getViewState().showLoading())
                    .subscribe(excursions -> {
                        if (excursions.size() == 0) {
                            getViewState().showEmptyExcursions();
                        } else {
                            getViewState().showExcursions(new ArrayList<>());
                        }
                    }, throwable -> {
                        if (throwable instanceof BaseException) {
                            if (throwable instanceof NetworkUnavailableException) {
                                getViewState().showNetworkUnavailable();
                            } else if (throwable instanceof ServerException) {
                                if (throwable instanceof ServerNotAuthorizedException) {
                                    getViewState().showNotAuthorizedException();
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
}
