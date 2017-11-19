package com.zighter.zighterandroid.presentation.map;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.zighter.zighterandroid.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class NavigationPresenter extends MvpPresenter<NavigationView> {


    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        getViewState().showLoading();

        Completable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getViewState().showRoute());
    }


}
