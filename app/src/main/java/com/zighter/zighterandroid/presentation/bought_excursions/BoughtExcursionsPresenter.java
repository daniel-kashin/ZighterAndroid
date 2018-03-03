package com.zighter.zighterandroid.presentation.bought_excursions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.TagConstraint;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.download_excursion.DownloadExcursionJob;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.repositories.excursion.ExcursionRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;

import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionProgressContract.ACTION_DOWNLOAD_CANCELLED;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionProgressContract.ACTION_DOWNLOAD_EXCEPTION;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionProgressContract.ACTION_DOWNLOAD_STARTED;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionProgressContract.ACTION_DOWNLOAD_SUCCESS;
import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionProgressContract.EXTRA_EXCURSION;

@InjectViewState
public class BoughtExcursionsPresenter extends BasePresenter<BoughtExcursionsView> {
    private static final String TAG = "BoughtExcursionsPresenter";

    @NonNull
    private final Context applicationContext;
    @NonNull
    private final ExcursionRepository excursionRepository;
    @NonNull
    private final JobManager jobManager;
    @NonNull
    private final DownloadExcursionProgressBroadcastReceiver receiver;

    public BoughtExcursionsPresenter(@NonNull Context applicationContext,
                                     @NonNull ExcursionRepository excursionRepository,
                                     @NonNull JobManager jobManager,
                                     @NonNull Scheduler worker,
                                     @NonNull Scheduler ui) {
        super(worker, ui);
        this.applicationContext = applicationContext;
        this.excursionRepository = excursionRepository;
        this.jobManager = jobManager;

        receiver = new DownloadExcursionProgressBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DOWNLOAD_STARTED);
        intentFilter.addAction(ACTION_DOWNLOAD_EXCEPTION);
        intentFilter.addAction(ACTION_DOWNLOAD_SUCCESS);
        intentFilter.addAction(ACTION_DOWNLOAD_CANCELLED);
        applicationContext.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        onReloadExcursionsRequest();
    }

    @Override
    public void onDestroy() {
        applicationContext.unregisterReceiver(receiver);
        super.onDestroy();
    }

    void onReloadExcursionsRequest() {
        getViewState().showLoading();
        excursionRepository.getBoughtExcursions()
                .compose(applySchedulersSingle())
                .subscribe(excursions -> {
                    getViewState().showExcursions(excursions);
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

    void onDownloadClicked(@NonNull BoughtExcursion boughtExcursion) {
        jobManager.addJobInBackground(new DownloadExcursionJob(boughtExcursion));
    }

    public class DownloadExcursionProgressBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("PresenterBR", "onReceive(" + (intent == null ? "null" : intent.getAction()) + ")");
            if (context == null || intent == null || intent.getAction() == null) {
                return;
            }

            switch (intent.getAction()) {
                case ACTION_DOWNLOAD_STARTED: {
                    break;
                }
                case ACTION_DOWNLOAD_SUCCESS: {
                    break;
                }
                case ACTION_DOWNLOAD_EXCEPTION: {
                    break;
                }
            }
        }
    }
}
