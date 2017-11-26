package com.zighter.zighterandroid.util;

import android.support.annotation.NonNull;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerHelper {

    @NonNull
    public static <T> ObservableTransformer<T,T> applySchedulersObservable(Scheduler worker, Scheduler ui) {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NonNull
    public static <T> SingleTransformer<T,T> applySchedulersSingle(Scheduler worker, Scheduler ui) {
        return single -> single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
