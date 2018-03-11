package com.zighter.zighterandroid.presentation.bought_excursions;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.job_manager.JobManagerEventsBroadcastReceiver;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import static com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus.DownloadStatus;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

@InjectViewState
public class BoughtExcursionsPresenter extends BasePresenter<BoughtExcursionsView> {
    private static final String TAG = "BoughtExcPresenter";

    @NonNull
    private final Context applicationContext;
    @NonNull
    private final ExcursionRepository excursionRepository;
    @Nullable
    private Disposable getExcursionsDisposable;
    @Nullable
    private Disposable subscribeOnDownloadDisposable;

    public BoughtExcursionsPresenter(@NonNull Context applicationContext,
                                     @NonNull ExcursionRepository excursionRepository,
                                     @NonNull Scheduler worker,
                                     @NonNull Scheduler ui) {
        super(worker, ui);
        this.applicationContext = applicationContext;
        this.excursionRepository = excursionRepository;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        onReloadExcursionsRequest();
    }

    @Override
    public void onDestroy() {
        if (getExcursionsDisposable != null) {
            getExcursionsDisposable.dispose();
        }

        if (subscribeOnDownloadDisposable != null && !subscribeOnDownloadDisposable.isDisposed()) {
            subscribeOnDownloadDisposable.dispose();
        }
        subscribeOnDownloadDisposable = null;

        super.onDestroy();
    }

    void onReloadExcursionsRequest() {
        getExcursionsDisposable = excursionRepository
                .getBoughtExcursions()
                .compose(applySchedulersSingle())
                .doOnSubscribe(disposable -> getViewState().showLoading())
                .subscribe(excursions -> {
                    if (!excursions.isEmpty()) {
                        getViewState().showExcursions(excursions);
                        subscribeOnDownloadActions();
                    } else {
                        getViewState().showEmptyExcursions();
                    }
                }, throwable -> {
                    if (throwable instanceof BaseException) {
                        if (throwable instanceof NetworkUnavailableException) {
                            getViewState().showNetworkUnavailable();
                        } else if (throwable instanceof ServerException) {
                            getViewState().showServerException();
                        }
                    } else {
                        getViewState().showUnhandledException();
                        handleThrowable(throwable, TAG);
                    }
                });
    }

    void onDownloadClicked(@NonNull BoughtExcursionWithStatus boughtExcursionWithStatus) {
        DownloadStatus downloadStatus = boughtExcursionWithStatus.getStatus();
        BoughtExcursion boughtExcursion = boughtExcursionWithStatus.getExcursion();
        if (downloadStatus == DownloadStatus.IDLE) {
            excursionRepository.addDownloadExcursionJob(boughtExcursion);
        } else if (downloadStatus == DownloadStatus.DOWNLOADING) {
            excursionRepository.removeDownloadExcursionJob(boughtExcursion);
        }
    }

    private void subscribeOnDownloadActions() {
        if (subscribeOnDownloadDisposable == null) {
            subscribeOnDownloadDisposable = excursionRepository
                    .subscribeOnDownloadExcursionEvents()
                    .observeOn(ui)
                    .subscribe(pair -> {
                        String boughtExcursionId = pair.first;
                        JobManagerEventsBroadcastReceiver.Event event = pair.second;
                        DownloadStatus downloadStatus = null;
                        switch (event) {
                            case Added:
                                Log.d(TAG, "Added(" + boughtExcursionId + ")");
                            case Started:
                                Log.d(TAG, "Started(" + boughtExcursionId + ")");
                                downloadStatus = DownloadStatus.DOWNLOADING;
                                break;
                            case Exception:
                                Log.d(TAG, "Exception(" + boughtExcursionId + ")");
                                downloadStatus = DownloadStatus.IDLE;
                                break;
                            case Cancelled:
                                Log.d(TAG, "Cancelled(" + boughtExcursionId + ")");
                                downloadStatus = DownloadStatus.IDLE;
                                break;
                            case Success:
                                Log.d(TAG, "Success(" + boughtExcursionId + ")");
                                downloadStatus = DownloadStatus.DOWNLOADED;
                                break;
                        }

                        if (downloadStatus != null) {
                            getViewState().showExcursionStatus(boughtExcursionId, downloadStatus);
                        }
                    });
        }
    }
}
