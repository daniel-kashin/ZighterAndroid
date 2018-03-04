@file:JvmName("SchedulerHelper")

package com.zighter.zighterandroid.util

import io.reactivex.CompletableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> applySchedulersObservable(worker: Scheduler, ui: Scheduler): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable.subscribeOn(worker).observeOn(ui)
    }
}

fun <T> applySchedulersSingle(worker: Scheduler, ui: Scheduler): SingleTransformer<T, T> {
    return SingleTransformer { single ->
        single.subscribeOn(worker).observeOn(ui)
    }
}

fun <T> applySchedulersCompletable(worker: Scheduler, ui: Scheduler): CompletableTransformer {
    return CompletableTransformer { completable ->
        completable.subscribeOn(worker).observeOn(ui)
    }
}
