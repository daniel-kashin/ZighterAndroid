package com.zighter.zighterandroid.presentation.guide;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;

public class GuideActivity extends BaseSupportActivity {
    public static void start(@NonNull Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    @Override
    protected void onInjectDependencies() {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
