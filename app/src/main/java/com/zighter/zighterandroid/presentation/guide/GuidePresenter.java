package com.zighter.zighterandroid.presentation.guide;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.presentation.common.BasePresenter;

import io.reactivex.Scheduler;

public class GuidePresenter extends BasePresenter<GuideView> {
    public GuidePresenter(@NonNull Scheduler worker, @NonNull Scheduler ui) {
        super(worker, ui);
    }
}
