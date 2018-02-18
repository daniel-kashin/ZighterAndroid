package com.zighter.zighterandroid.presentation.media.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static com.zighter.zighterandroid.presentation.media.adapter.ThumbnailMediaAdapter.ThumbnailViewHolder;
import static com.zighter.zighterandroid.util.recyclerview.UnitConverter.dpToPx;

public class ThumbnailMediaAdapter extends MediaAdapter<ThumbnailViewHolder> {
    private static final String TAG = "ThumbnailMediaAdapter";

    @Nullable
    private OnClickListener onClickListener;

    private int currentSelectedPosition = NO_POSITION;

    ThumbnailMediaAdapter(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    void onDestroy() {
        onClickListener = null;
    }

    void setCurrentSelectedPosition(int position) {
        if (currentSelectedPosition != position) {
            int oldPosition = currentSelectedPosition;
            currentSelectedPosition = position;

            if (oldPosition != NO_POSITION) {
                notifyItemChanged(oldPosition);
            }
            if (currentSelectedPosition != NO_POSITION) {
                notifyItemChanged(position);
            }
        }
    }

    @Override
    public ThumbnailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thumbnail, parent, false);

        return new ThumbnailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThumbnailViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder(" + position + ")");

        DrawableMedia media = getItemAt(position);

        holder.setOnRootViewClickListener(it -> {
            if (onClickListener != null) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != NO_POSITION) {
                    onClickListener.onThumbnailClicked(adapterPosition);
                }
            }
        });

        boolean isCurrent = currentSelectedPosition == position;
        holder.bind(media, isCurrent);
    }

    static final class ThumbnailViewHolder extends RecyclerView.ViewHolder {
        private static final int DEFAULT_MARGIN_IN_DP = 1;
        private static final int BIG_MARGIN_ID_DP = 9;
        private final int defaultMarginInPx;
        private final int bigMarginInPx;

        @BindView(R.id.root_view)
        RelativeLayout rootView;
        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        @NonNull
        private final Context context;

        @NonNull
        private final RequestOptions requestOptions;

        ThumbnailViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);

            context = rootView.getContext();
            defaultMarginInPx = dpToPx(context, DEFAULT_MARGIN_IN_DP);
            bigMarginInPx = dpToPx(context, BIG_MARGIN_ID_DP);

            int transparentColor = rootView.getResources().getColor(android.R.color.transparent);
            ColorDrawable placeholder = new ColorDrawable(transparentColor);
            requestOptions = new RequestOptions()
                    .centerCrop()
                    .error(placeholder)
                    .fallback(placeholder)
                    .placeholder(placeholder)
                    .frame(0)
                    .diskCacheStrategy(DiskCacheStrategy.DATA);
        }

        void setOnRootViewClickListener(@NonNull View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }

        void bind(@NonNull DrawableMedia media, boolean isCurrent) {
            final ViewGroup.LayoutParams params = rootView.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) params;

                ValueAnimator animator = null;
                if (isCurrent) {
                    animator = ValueAnimator.ofFloat(marginParams.leftMargin, bigMarginInPx);
                    animator.setDuration(100);
                } else if (defaultMarginInPx != marginParams.leftMargin) {
                    animator = ValueAnimator.ofFloat(marginParams.leftMargin, defaultMarginInPx);
                    animator.setDuration(100);
                }


                if (animator != null) {
                    animator.addUpdateListener(it -> {
                        int margin = Math.round((Float) it.getAnimatedValue());
                        marginParams.leftMargin = margin;
                        marginParams.rightMargin = margin;
                        rootView.requestLayout();
                    });
                    animator.start();
                }
            }

            if (media instanceof Video) {
                Glide.with(context)
                        .asBitmap()
                        .load(media.getUrl())
                        .apply(requestOptions)
                        .into(thumbnail);
            } else if (media instanceof Image) {
                Glide.with(context)
                        .load(media.getUrl())
                        .apply(requestOptions)
                        .into(thumbnail);
            }
        }
    }

    interface OnClickListener {
        void onThumbnailClicked(int position);
    }
}
