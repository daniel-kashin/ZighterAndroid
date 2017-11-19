package com.zighter.zighterandroid.presentation.common;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import com.arellomobile.mvp.MvpAppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseSupportActivity extends MvpAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        onInjectDependencies();
        ButterKnife.bind(this);
    }

    protected abstract void onInjectDependencies();

    @LayoutRes
    protected abstract int getLayoutId();

}
