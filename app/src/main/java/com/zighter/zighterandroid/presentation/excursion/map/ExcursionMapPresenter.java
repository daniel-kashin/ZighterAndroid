package com.zighter.zighterandroid.presentation.excursion.map;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.data.exception.BaseException;
import com.zighter.zighterandroid.data.exception.NetworkUnavailableException;
import com.zighter.zighterandroid.data.exception.ServerException;
import com.zighter.zighterandroid.data.repositories.paths.PathsRepository;
import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;

@SuppressWarnings("CodeBlock2Expr")
@InjectViewState
public class ExcursionMapPresenter extends BasePresenter<ExcursionMapView> {

    private static final String TAG = "[ExcursionMapPresenter]";

    @NonNull
    private final PathsRepository pathsRepository;

    public ExcursionMapPresenter(@NonNull PathsRepository pathsRepository,
                                 @NonNull Scheduler worker,
                                 @NonNull Scheduler ui) {
        super(worker, ui);
        this.pathsRepository = pathsRepository;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showLoading();

        pathsRepository.getExcursion()
                .compose(applySchedulersSingle())
                .subscribe(excursion -> {
                    getViewState().showExcursion(excursion);
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

    void onSightClicked(@NonNull Sight sight, @NonNull Marker marker) {
        getViewState().showSightSelection(sight, marker);
    }


}
