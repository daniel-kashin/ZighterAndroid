package com.zighter.zighterandroid.util.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ThumbnailItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }

        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition == 0) {
            outRect.left = UnitConverter.dpToPx(parent.getContext(), 20);
        } else if (childAdapterPosition == adapter.getItemCount() - 1) {
            outRect.right = UnitConverter.dpToPx(parent.getContext(), 20);
        }
    }
}
