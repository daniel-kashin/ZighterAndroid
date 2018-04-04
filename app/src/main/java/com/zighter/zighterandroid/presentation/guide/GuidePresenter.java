package com.zighter.zighterandroid.presentation.guide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class GuidePresenter extends BasePresenter<GuideView> {
    private static final String TAG = "GuidePresenter";

    @NonNull
    private final ExcursionRepository excursionRepository;
    @NonNull
    private final String ownerUuid;

    @Nullable
    private Disposable loadGuideDisposable;

    GuidePresenter(@NonNull Scheduler worker,
                   @NonNull Scheduler ui,
                   @NonNull ExcursionRepository excursionRepository,
                   @NonNull String ownerUuid) {
        super(worker, ui);
        this.excursionRepository = excursionRepository;
        this.ownerUuid = ownerUuid;
    }

    @Override
    public void onDestroy() {
        if (loadGuideDisposable != null) {
            loadGuideDisposable.dispose();
            loadGuideDisposable = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        onLoadGuide();
    }

    void onLoadGuide() {
        if (loadGuideDisposable != null) {
            loadGuideDisposable.dispose();
            loadGuideDisposable = null;
        }

        loadGuideDisposable = excursionRepository.getGuide(ownerUuid)
                .compose(applySchedulersSingle())
                .doOnSubscribe(disposable -> getViewState().showLoading())
                .subscribe(guide -> {
                    getViewState().showGuide(guide);
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

    public static class GuidePresenterBuilder {
        @NonNull
        private final Scheduler worker;
        @NonNull
        private final Scheduler ui;
        @NonNull
        private final ExcursionRepository excursionRepository;

        private String ownerUuid;

        public GuidePresenterBuilder(@NonNull Scheduler worker,
                                     @NonNull Scheduler ui,
                                     @NonNull ExcursionRepository excursionRepository) {
            this.worker = worker;
            this.ui = ui;
            this.excursionRepository = excursionRepository;
        }

        @NonNull
        public GuidePresenterBuilder ownerUuid(@NonNull String ownerUuid) {
            this.ownerUuid = ownerUuid;
            return this;
        }

        @NonNull
        public GuidePresenter build() {
            if (ownerUuid == null) {
                throw new IllegalStateException();
            }

            return new GuidePresenter(worker, ui, excursionRepository, ownerUuid);
        }
    }
}
