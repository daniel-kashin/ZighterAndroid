package com.zighter.zighterandroid.presentation.account;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;

import butterknife.BindView;

public class AccountFragment extends BaseSupportFragment {

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @BindView(R.id.video_view)
    VideoView videoView;

    @Override
    protected void onInjectDependencies() {
        // do nothing
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
