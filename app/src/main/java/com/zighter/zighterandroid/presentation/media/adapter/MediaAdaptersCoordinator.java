package com.zighter.zighterandroid.presentation.media.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.View;

import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.util.recyclerview.FullscreenScrollListener;
import com.zighter.zighterandroid.util.recyclerview.HorizontalSpaceItemDecoration;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

public class MediaAdaptersCoordinator implements FullscreenScrollListener.OnSwipeListener {
    private static final String TAG = "Coordinator";

    @NonNull
    private final RecyclerView fullscreenMedia;
    @NonNull
    private final FullscreenMediaAdapter fullscreenMediaAdapter;
    private View.OnLayoutChangeListener onFullscreenAdapterLayoutChangeListener;

    public MediaAdaptersCoordinator(@NonNull RecyclerView fullscreenMedia) {
        this.fullscreenMedia = fullscreenMedia;
        this.fullscreenMediaAdapter = new FullscreenMediaAdapter();
        initializeFullscreenMedia();

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
    }

    private void initializeFullscreenMedia() {
        fullscreenMedia.setAdapter(fullscreenMediaAdapter);

        fullscreenMedia.setLayoutManager(new LinearLayoutManager(fullscreenMedia.getContext(),
                                                                 LinearLayoutManager.HORIZONTAL,
                                                                 false));

        fullscreenMedia.addItemDecoration(new HorizontalSpaceItemDecoration(25));

        FullscreenScrollListener previewSwipeListener = new FullscreenScrollListener(this);
        fullscreenMedia.addOnScrollListener(previewSwipeListener);

        fullscreenMedia.setItemAnimator(null);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(fullscreenMedia);
    }

    public void setMedias(@NonNull List<DrawableMedia> medias) {
        fullscreenMediaAdapter.setMedias(medias);
        if (!medias.isEmpty()) {
            fullscreenMedia.addOnLayoutChangeListener(onFullscreenAdapterLayoutChangeListener);
            fullscreenMediaAdapter.notifyDataSetChanged();
        }
    }

    public void onDestroy() {
        fullscreenMediaAdapter.onDestroy();
    }

    @Override
    public void onSwiped(int currentPosition) {
        Log.d(TAG, "onSwiped(" + currentPosition + ")");
        fullscreenMediaAdapter.setCurrentSelectedPosition(currentPosition);
    }
}
