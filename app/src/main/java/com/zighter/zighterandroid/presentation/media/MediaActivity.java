package com.zighter.zighterandroid.presentation.media;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.excursion.Sight;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
import com.zighter.zighterandroid.presentation.media.adapter.MediaAdaptersCoordinator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

public class MediaActivity extends BaseSupportActivity implements MediaView {
    private static final String KEY_SIGHT = "SIGHT";

    public static void start(@NonNull Context context, @NonNull Sight sight) {
        Intent intent = new Intent(context, MediaActivity.class);
        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_SIGHT, sight);
        intent.putExtras(arguments);
        context.startActivity(intent);
    }

    @InjectPresenter
    MediaPresenter presenter;

    @ProvidePresenter
    public MediaPresenter providePresenter() {
        if (sight == null) {
            throw new IllegalStateException();
        }

        return mediaPresenterBuilderProvider.get()
                .sight(sight)
                .build();
    }

    @Inject
    Provider<MediaPresenter.Builder> mediaPresenterBuilderProvider;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getMediaComponent()
                .inject(this);
    }

    @BindView(R.id.fullscreen_media)
    RecyclerView fullscreenMedia;
    MediaAdaptersCoordinator mediaAdaptersCoordinator;

    private Sight sight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeSight();
        super.onCreate(savedInstanceState);
        mediaAdaptersCoordinator = new MediaAdaptersCoordinator(fullscreenMedia);
    }

    @Override
    protected void onDestroy() {
        mediaAdaptersCoordinator.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onFullyDestroy() {
    }

    private void initializeSight() {
        Bundle arguments = getIntent().getExtras();
        if (arguments == null || arguments.getSerializable(KEY_SIGHT) == null) {
            throw new IllegalStateException();
        }
        sight = (Sight) arguments.getSerializable(KEY_SIGHT);
    }

    @Override
    public void setMedias(@NonNull List<DrawableMedia> medias) {
        mediaAdaptersCoordinator.setMedias(medias);
    }
}
