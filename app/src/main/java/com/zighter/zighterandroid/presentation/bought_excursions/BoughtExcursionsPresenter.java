package com.zighter.zighterandroid.presentation.bought_excursions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.job_manager.JobManagerWrapper;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import static com.zighter.zighterandroid.data.entities.excursion.BoughtExcursionWithStatus.DownloadStatus;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;

import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_CANCELLED;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_EXCEPTION;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_STARTED;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.ACTION_SUCCESS;
import static com.zighter.zighterandroid.data.job_manager.JobManagerProgressContract.EXTRA_EXCURSION;

@InjectViewState
public class BoughtExcursionsPresenter extends BasePresenter<BoughtExcursionsView> {
    private static final String TAG = "BoughtExcursionsPresenter";

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
        if (subscribeOnDownloadDisposable != null) {
            subscribeOnDownloadDisposable.dispose();
        }

        super.onDestroy();
    }

    void onReloadExcursionsRequest() {
        getViewState().showLoading();
        getExcursionsDisposable = excursionRepository.getBoughtExcursions()
                .compose(applySchedulersSingle())
                .subscribe(excursions -> {
                    getViewState().showExcursions(excursions);
                    subscribeOnDownloadActions();
                }, throwable -> {
                    if (throwable instanceof BaseException) {
                        if (throwable instanceof NetworkUnavailableException) {
                            getViewState().showNetworkUnavailable();
                        } else if (throwable instanceof ServerException) {
                            getViewState().showServerException();
                        }
                    } else {
                        handleThrowable(throwable, TAG);
                    }
                });
    }

    void onDownloadClicked(@NonNull BoughtExcursionWithStatus boughtExcursionWithStatus) {
        DownloadStatus downloadStatus = boughtExcursionWithStatus.getStatus();
        BoughtExcursion boughtExcursion = boughtExcursionWithStatus.getExcursion();
        if (downloadStatus == DownloadStatus.IDLE) {
            excursionRepository.addDownloadExcursionJob(boughtExcursion)
                    .compose(applySchedulersCompletable())
                    .subscribe(() -> {
                        getViewState().showExcursionStatus(boughtExcursion.getUuid(), DownloadStatus.DOWNLOADING);
                    });
        }
    }

    private void subscribeOnDownloadActions() {
        if (subscribeOnDownloadDisposable == null) {
            subscribeOnDownloadDisposable = excursionRepository.subscribeOnDownloadExcursionEvents()
                    .subscribe(pair -> {
                        String boughtExcursionId = pair.first;
                        JobManagerWrapper.Action action = pair.second;
                        DownloadStatus downloadStatus = null;
                        switch (action) {
                            case Started:
                                downloadStatus = DownloadStatus.DOWNLOADING;
                                break;
                            case Exception:
                            case Cancelled:
                                downloadStatus = DownloadStatus.IDLE;
                                break;
                            case Success:
                                downloadStatus = DownloadStatus.DOWNLOADED;
                                break;
                        }

                        if (downloadStatus != null) {
                            getViewState().showExcursionStatus(boughtExcursionId, downloadStatus);
                        }
                    });
        }
    }

    public class DownloadExcursionProgressBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PresenterBR", "onReceive(" + (intent == null ? "null" : intent.getAction()) + ")");
            if (context == null || intent == null || intent.getAction() == null) {
                return;
            }

            BoughtExcursion boughtExcursion = (BoughtExcursion) intent.getSerializableExtra(EXTRA_EXCURSION);
            if (boughtExcursion == null) {
                return;
            }

            switch (intent.getAction()) {
                case ACTION_STARTED: {
                    getViewState().showExcursionStatus(boughtExcursion.getUuid(), DownloadStatus.DOWNLOADING);
                    break;
                }
                case ACTION_SUCCESS: {
                    getViewState().showExcursionStatus(boughtExcursion.getUuid(), DownloadStatus.DOWNLOADED);
                    break;
                }
                case ACTION_EXCEPTION: {
                    getViewState().showExcursionStatus(boughtExcursion.getUuid(), DownloadStatus.IDLE);
                    break;
                }
                case ACTION_CANCELLED: {
                    getViewState().showExcursionStatus(boughtExcursion.getUuid(), DownloadStatus.IDLE);
                    break;
                }
            }
        }
    }
}
