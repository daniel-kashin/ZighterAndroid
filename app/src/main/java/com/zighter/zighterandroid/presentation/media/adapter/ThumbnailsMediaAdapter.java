package com.zighter.zighterandroid.presentation.media.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zighter.zighterandroid.presentation.media.adapter.ThumbnailsMediaAdapter.ViewType.IMAGE;
import static com.zighter.zighterandroid.presentation.media.adapter.ThumbnailsMediaAdapter.ViewType.VIDEO;

public class ThumbnailsMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @Nullable
    private List<DrawableMedia> medias;

    public ThumbnailsMediaAdapter() {
    }

    public void setMedias(@NonNull List<DrawableMedia> medias) {
        this.medias = medias;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int resourceId) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false);

        if (resourceId == VIDEO.resourceId) {
            return new VideoViewHolder(view);
        } else if (resourceId == IMAGE.resourceId) {
            return new ImageViewHolder(view);
        }

        throw new IllegalStateException();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (medias == null) {
            throw new IllegalStateException();
        }

        Media media = medias.get(position);

        if (holder instanceof VideoViewHolder && media instanceof Video) {
            ((VideoViewHolder) holder).bind((Video) media);
        }

        if (holder instanceof ImageViewHolder && media instanceof Image) {
            ((ImageViewHolder) holder).bind((Image) media);
        }
    }

    @Override
    public int getItemCount() {
        return medias != null ? medias.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (medias == null) {
            throw new IllegalStateException();
        }
        DrawableMedia media = medias.get(position);

        if (media instanceof Video) {
            return VIDEO.resourceId;
        }

        if (media instanceof Image) {
            return IMAGE.resourceId;
        }

        throw new IllegalStateException();
    }

    static final class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_view)
        View rootView;
        @BindView(R.id.image_view)
        ImageView imageView;

        ImageViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(@NonNull Image image) {

        }
    }

    static final class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_view)
        View rootView;
        @BindView(R.id.image_view)
        ImageView imageView;

        VideoViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(@NonNull Video video) {
            Glide.with(imageView.getContext())
                    .asBitmap()
                    .load(video.getUrl())
                    .into(imageView);
        }
    }

    enum ViewType {
        VIDEO(R.layout.item_thumbnail_video),
        IMAGE(R.layout.item_thumbnail_image);

        int resourceId;

        ViewType(int resourceId) {
            this.resourceId = resourceId;
        }
    }
}
