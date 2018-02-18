package com.zighter.zighterandroid.presentation.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.excursion.Sight;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
import com.zighter.zighterandroid.presentation.media.adapter.MediaAdaptersCoordinator;
import com.zighter.zighterandroid.util.animation.ControlsAnimator;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

import static com.zighter.zighterandroid.presentation.media.adapter.MediaAdaptersCoordinator.OnMediaPositionChangeListener;
import static com.zighter.zighterandroid.presentation.media.adapter.MediaAdaptersCoordinator.OnFullscreenMediaClickListener;


public class MediaActivity
        extends BaseSupportActivity
        implements MediaView, OnMediaPositionChangeListener, OnFullscreenMediaClickListener {
    private static final String KEY_SIGHT = "SIGHT";

    private static final String KEY_ANIMATION_TOOLBAR = "KEY_ANIMATION_TOOLBAR";
    private static final String KEY_ANIMATION_FOOTER = "KEY_ANIMATION_FOOTER";

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

    @BindView(R.id.root_view)
    ConstraintLayout rootView;
    @BindView(R.id.fullscreen_media)
    RecyclerView fullscreenMedia;
    @BindView(R.id.thumbnail_media)
    RecyclerView thumbnailMedia;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_count)
    TextView toolbarTitleCount;
    @BindView(R.id.toolbar_title_name)
    TextView toolbarTitleName;
    @BindView(R.id.icon_text)
    ImageView iconText;
    @BindView(R.id.description_scroll)
    ScrollView descriptionScroll;
    @BindView(R.id.text_description)
    TextView textDescription;
    @BindView(R.id.footer)
    View footer;
    MediaAdaptersCoordinator mediaAdaptersCoordinator;

    private boolean isToolbarShown = true;
    private boolean isFooterShown = true;

    private ControlsAnimator controlsAnimator;
    private Sight sight;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_media;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeSight();
        super.onCreate(savedInstanceState);
        initializeView();
        initializeControls();
        mediaAdaptersCoordinator = new MediaAdaptersCoordinator(fullscreenMedia,
                                                                thumbnailMedia,
                                                                this,
                                                                this);
    }

    private void initializeControls() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        controlsAnimator = new ControlsAnimator.Builder()
                .addGroup(KEY_ANIMATION_TOOLBAR,
                          it -> -it.getHeight(),
                          ControlsAnimator.Axis.VERTICAL,
                          toolbar)
                .addGroup(KEY_ANIMATION_FOOTER,
                          it -> screenHeight + it.getHeight(),
                          ControlsAnimator.Axis.VERTICAL,
                          footer,
                          thumbnailMedia)
                .build();

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                controlsAnimator.initPositions();
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void initializeSight() {
        Bundle arguments = getIntent().getExtras();
        if (arguments == null || arguments.getSerializable(KEY_SIGHT) == null) {
            throw new IllegalStateException();
        }
        sight = (Sight) arguments.getSerializable(KEY_SIGHT);
    }

    private void initializeView() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitleCount.setVisibility(View.GONE);
        toolbarTitleName.setVisibility(View.GONE);
        iconText.setVisibility(View.GONE);
        descriptionScroll.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        mediaAdaptersCoordinator.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (descriptionScroll.getVisibility() == View.VISIBLE) {
            presenter.onHideDescription();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void setMedias(@NonNull List<DrawableMedia> medias) {
        mediaAdaptersCoordinator.setMedias(medias);
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void showCurrentMediaPosition(int currentPosition, int size, boolean isIconTextShown) {
        //noinspection ConstantConditions
        toolbarTitleCount.setText((currentPosition + 1) + " of " + size);
        if (descriptionScroll.getVisibility() != View.VISIBLE) {
            toolbarTitleCount.setVisibility(View.VISIBLE);
        }

        if (isIconTextShown) {
            iconText.setOnClickListener(view -> {
                presenter.onIconTextClicked();
            });
            if (descriptionScroll.getVisibility() != View.VISIBLE) {
                iconText.setVisibility(View.VISIBLE);
            }
        } else {
            iconText.setVisibility(View.GONE);
            iconText.setOnClickListener(null);
        }
    }

    @Override
    public void showMediaDescription(@Nullable String name, @NonNull String body) {
        iconText.setVisibility(View.GONE);
        toolbarTitleCount.setVisibility(View.GONE);

        toolbarTitleName.setText(name);
        textDescription.setText(body);

        toolbarTitleName.setVisibility(View.VISIBLE);
        descriptionScroll.setVisibility(View.VISIBLE);

        if (isFooterShown) {
            isFooterShown = false;
            controlsAnimator.hideControls(KEY_ANIMATION_FOOTER);
        }
    }

    @Override
    public void hideMediaDescription(boolean isIconTextShown) {
        toolbarTitleName.setVisibility(View.GONE);
        descriptionScroll.setVisibility(View.GONE);

        toolbarTitleCount.setVisibility(View.VISIBLE);

        if (!isFooterShown) {
            isFooterShown = true;
            controlsAnimator.showControls(KEY_ANIMATION_FOOTER);
        }

        if (isIconTextShown) {
            iconText.setVisibility(View.VISIBLE);
            iconText.setOnClickListener(view -> {
                presenter.onIconTextClicked();
            });
        }
    }

    @Override
    public void onMediaPositionChanged(int currentPosition) {
        presenter.onMediaPositionChanged(currentPosition);
    }

    @Override
    public void onFullscreenMediaClicked() {
        if (isToolbarShown) {
            isToolbarShown = false;
            controlsAnimator.hideControls(KEY_ANIMATION_TOOLBAR);
        } else {
            isToolbarShown = true;
            controlsAnimator.showControls(KEY_ANIMATION_TOOLBAR);
        }

        if (isFooterShown) {
            isFooterShown = false;
            controlsAnimator.hideControls(KEY_ANIMATION_FOOTER);
        } else {
            isFooterShown = true;
            controlsAnimator.showControls(KEY_ANIMATION_FOOTER);
        }
    }
}
