package com.zighter.zighterandroid.presentation.common;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.google.firebase.crash.FirebaseCrash;
import com.zighter.zighterandroid.util.SchedulerHelper;

import io.reactivex.CompletableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.SingleTransformer;

public abstract class BasePresenter<T extends MvpView> extends MvpPresenter<T> {
    @NonNull
    private final Scheduler worker;
    @NonNull
    private final Scheduler ui;

    public BasePresenter(@NonNull Scheduler worker,
                         @NonNull Scheduler ui) {
        this.worker = worker;
        this.ui = ui;
    }

    protected <P> ObservableTransformer<P,P> applySchedulersObservable() {
        return SchedulerHelper.applySchedulersObservable(worker, ui);
    }

    protected <P> SingleTransformer<P,P> applySchedulersSingle() {
        return SchedulerHelper.applySchedulersSingle(worker, ui);
    }

    protected <P>CompletableTransformer applySchedulersCompletable() {
        return SchedulerHelper.applySchedulersCompletable(worker, ui);
    }

    protected void handleThrowable(Throwable throwable, String tag) {
        FirebaseCrash.log(tag);
        FirebaseCrash.report(throwable);
    }
}
