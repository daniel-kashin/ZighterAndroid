package com.zighter.zighterandroid.util.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class FullscreenScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "ScrollListener";

    @NonNull
    private final OnSwipeListener listener;

    public FullscreenScrollListener(@NonNull OnSwipeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            throw new IllegalStateException();
        }

        int currentPosition = ((LinearLayoutManager)layoutManager).findLastCompletelyVisibleItemPosition();
        Log.d(TAG, "onSwiped(" + currentPosition + ")");
        if (newState == RecyclerView.SCROLL_STATE_IDLE && currentPosition != RecyclerView.NO_POSITION) {
            listener.onSwiped(currentPosition);
        }
    }

    public interface OnSwipeListener {
        void onSwiped(int currentPosition);
    }
}