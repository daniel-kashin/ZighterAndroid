package com.zighter.zighterandroid.presentation.search;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchSort;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

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
    @Nullable
    private Disposable bindRouteDisposable;

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

    void onBindCancelled() {
        if (bindRouteDisposable != null) {
            bindRouteDisposable.dispose();
            bindRouteDisposable = null;
        }
    }

    void onBindRoute(@NonNull String excursionUuid) {
        if (bindRouteDisposable != null) {
            bindRouteDisposable.dispose();
            bindRouteDisposable = null;
        }
        bindRouteDisposable = excursionRepository.bindExcursionRoute(excursionUuid)
                .compose(applySchedulersSingle())
                .doOnSubscribe(disposable -> getViewState().showExcursionBuyingLoading())
                .subscribe(response -> {
                    getViewState().showExcursionBought();
                }, throwable -> {
                    if (throwable instanceof BaseException) {
                        if (throwable instanceof NetworkUnavailableException) {
                            getViewState().showAddingNetworkUnavailable();
                        } else if (throwable instanceof ServerException) {
                            if (throwable instanceof ServerNotAuthorizedException) {
                                getViewState().showNotAuthorizedException();
                            } else {
                                getViewState().showServerAddingException();
                            }
                        }
                    } else {
                        handleThrowable(throwable, TAG);
                        getViewState().showServerAddingException();
                    }
                });
    }

    void onSearchTyped(@NonNull String text, @NonNull ServiceSearchSort serviceSearchSort) {
        if (getExcursionsDisposable != null) {
            getExcursionsDisposable.dispose();
        }

        if (text.isEmpty()) {
            getViewState().showEmptySearch();
        } else {
            getExcursionsDisposable = excursionRepository
                    .searchExcursions(text, serviceSearchSort)
                    .compose(applySchedulersSingle())
                    .doOnSubscribe(disposable -> getViewState().showLoading())
                    .subscribe(excursions -> {
                        if (!excursions.isEmpty()) {
                            getViewState().showExcursions(excursions);
                        } else {
                            getViewState().showEmptyExcursions();
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
