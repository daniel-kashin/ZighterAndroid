package com.zighter.zighterandroid.presentation.media.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.util.recyclerview.FullscreenScrollListener;
import com.zighter.zighterandroid.util.recyclerview.ThumbnailItemDecoration;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class MediaAdaptersCoordinator implements
        FullscreenScrollListener.OnSwipeListener,
        ThumbnailMediaAdapter.OnClickListener,
        FullscreenMediaAdapter.OnClickListener {

    private static final String TAG = "Coordinator";

    @NonNull
    private final RecyclerView fullscreenMedia;
    @NonNull
    private final RecyclerView thumbnailMedia;
    @NonNull
    private final FullscreenMediaAdapter fullscreenMediaAdapter;
    @NonNull
    private final ThumbnailMediaAdapter thumbnailMediaAdapter;
    @Nullable
    private View.OnLayoutChangeListener onFullscreenAdapterLayoutChangeListener;
    @Nullable
    private final OnMediaPositionChangeListener onMediaPositionChangeListener;
    @Nullable
    private final OnFullscreenMediaClickListener onFullscreenMediaClickListener;
    @Nullable
    private List<DrawableMedia> medias;

    private int currentPosition = NO_POSITION;

    public MediaAdaptersCoordinator(@NonNull RecyclerView fullscreenMedia,
                                    @NonNull RecyclerView thumbnailMedia,
                                    @Nullable OnMediaPositionChangeListener onMediaPositionChangeListener,
                                    @Nullable OnFullscreenMediaClickListener onFullscreenMediaClickListener,
                                    @Nullable FullscreenMediaAdapter.OnUploadListener onUploadListener,
                                    @NonNull ViewGroup mediaControllerView) {
        this.fullscreenMedia = fullscreenMedia;
        this.thumbnailMedia = thumbnailMedia;
        this.onMediaPositionChangeListener = onMediaPositionChangeListener;
        this.onFullscreenMediaClickListener = onFullscreenMediaClickListener;

        fullscreenMediaAdapter = new FullscreenMediaAdapter(this, onUploadListener, mediaControllerView);
        thumbnailMediaAdapter = new ThumbnailMediaAdapter(this);
        initializeFullscreenMedia();
        initializeThumbnailMedia();
    }

    private void initializeFullscreenMedia() {
        onFullscreenAdapterLayoutChangeListener = new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) fullscreenMedia.getLayoutManager();
                int currentPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (currentPosition != NO_POSITION) {
                    fullscreenMedia.removeOnLayoutChangeListener(this);
                    onSwiped(layoutManager.findFirstCompletelyVisibleItemPosition());
                }
            }
        };

        fullscreenMedia.setAdapter(fullscreenMediaAdapter);

        fullscreenMedia.setLayoutManager(new LinearLayoutManager(fullscreenMedia.getContext(),
                                                                 LinearLayoutManager.HORIZONTAL,
                                                                 false));

        FullscreenScrollListener previewSwipeListener = new FullscreenScrollListener(this);
        fullscreenMedia.addOnScrollListener(previewSwipeListener);

        fullscreenMedia.setItemAnimator(null);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(fullscreenMedia);
    }

    private void initializeThumbnailMedia() {
        thumbnailMedia.setAdapter(thumbnailMediaAdapter);

        thumbnailMedia.setLayoutManager(new LinearLayoutManager(fullscreenMedia.getContext(),
                                                                LinearLayoutManager.HORIZONTAL,
                                                                false));

        thumbnailMedia.setItemAnimator(null);

        thumbnailMedia.addItemDecoration(new ThumbnailItemDecoration());
    }

    public void setMedias(@NonNull List<DrawableMedia> medias) {
        this.medias = medias;

        fullscreenMediaAdapter.setMedias(medias);
        thumbnailMediaAdapter.setMedias(medias);
        if (!medias.isEmpty()) {
            fullscreenMedia.addOnLayoutChangeListener(onFullscreenAdapterLayoutChangeListener);
            fullscreenMediaAdapter.notifyDataSetChanged();
            thumbnailMediaAdapter.notifyDataSetChanged();
        }
    }

    public void onStart() {
        fullscreenMediaAdapter.onStart();
    }

    public void onPause() {
        fullscreenMediaAdapter.onPause();
    }

    public void onDestroy() {
        fullscreenMediaAdapter.onDestroy();
        thumbnailMediaAdapter.onDestroy();
    }

    @Override
    public void onFullscreenMediaClicked() {
        if (onFullscreenMediaClickListener != null) {
            onFullscreenMediaClickListener.onFullscreenMediaClicked();
        }
    }

    @Override
    public void onSwiped(int currentPosition) {
        Log.d(TAG, "onSwiped(" + currentPosition + ")");
        int previousPosition = this.currentPosition;
        this.currentPosition = currentPosition;
        onPositionChanged(previousPosition, this.currentPosition, false);
    }

    @Override
    public void onThumbnailClicked(int position) {
        Log.d(TAG, "onThumbnailClicked(" + position + ")");
        int previousPosition = this.currentPosition;
        this.currentPosition = position;
        onPositionChanged(previousPosition, this.currentPosition, true);
    }

    private void onPositionChanged(int previousPosition, int currentPosition, boolean fromThumbnail) {
        if (previousPosition != currentPosition) {
            fullscreenMediaAdapter.setCurrentSelectedPosition(currentPosition);
            thumbnailMediaAdapter.setCurrentSelectedPosition(currentPosition);

            if (currentPosition != NO_POSITION && currentPosition >= 0 && medias != null && currentPosition < medias.size()) {
                if (fromThumbnail) {
                    fullscreenMedia.scrollToPosition(currentPosition);
                }

                int positionToScrollThumbnail = currentPosition;
                if (previousPosition != NO_POSITION) {
                    if (currentPosition > previousPosition) {
                        int thumbnailSize = thumbnailMediaAdapter.getItemCount();
                        for (int i = 0; i < 2; ++i) {
                            if (positionToScrollThumbnail + 1 < thumbnailSize) {
                                positionToScrollThumbnail++;
                            }
                        }
                    }

                    if (currentPosition < previousPosition) {
                        for (int i = 0; i < 2; ++i) {
                            if (positionToScrollThumbnail - 1 >= 0) {
                                positionToScrollThumbnail--;
                            }
                        }
                    }
                }

                thumbnailMedia.smoothScrollToPosition(positionToScrollThumbnail);

                if (onMediaPositionChangeListener != null) {
                    onMediaPositionChangeListener.onMediaPositionChanged(currentPosition);
                }
            }
        }
    }

    public interface OnMediaPositionChangeListener {
        void onMediaPositionChanged(int currentPosition);
    }

    public interface OnFullscreenMediaClickListener {
        void onFullscreenMediaClicked();
    }
}
