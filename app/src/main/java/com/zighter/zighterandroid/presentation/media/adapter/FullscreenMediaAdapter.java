package com.zighter.zighterandroid.presentation.media.adapter;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;
import com.zighter.zighterandroid.util.media.MediaPlayerHolder;
import com.zighter.zighterandroid.util.media.TextureViewHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static com.zighter.zighterandroid.presentation.media.adapter.FullscreenMediaAdapter.ViewType.IMAGE;
import static com.zighter.zighterandroid.presentation.media.adapter.FullscreenMediaAdapter.ViewType.VIDEO;

public class FullscreenMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "FullscreenMediaAdapter";
    private static final int KEY_NEW_SELECTED_POSITION = 111;
    private static final int KEY_PREVIOUS_SELECTED_POSITION = 112;

    @Nullable
    private List<DrawableMedia> medias;
    @NonNull
    private MediaPlayer mediaPlayer;
    private int currentSelectedPosition;

    FullscreenMediaAdapter() {
        this.mediaPlayer = new MediaPlayer();
        currentSelectedPosition = NO_POSITION;
    }

    void setMedias(@NonNull List<DrawableMedia> medias) {
        Log.d(TAG, "setMedias");
        this.medias = medias;
    }

    void setCurrentSelectedPosition(int position) {
        Log.d(TAG, "setCurrentSelectedPosition(" + position + ")");
        if (currentSelectedPosition != position) {
            if (currentSelectedPosition != NO_POSITION) {
                notifyItemChanged(currentSelectedPosition, KEY_PREVIOUS_SELECTED_POSITION);
            }
            currentSelectedPosition = position;
            if (currentSelectedPosition != NO_POSITION) {
                notifyItemChanged(currentSelectedPosition, KEY_NEW_SELECTED_POSITION);
            }
        }
    }

    void onDestroy() {
        mediaPlayer.release();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int resourceId) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false);

        if (resourceId == VIDEO.resourceId) {
            return new VideoViewHolder(view, mediaPlayer);
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
            Log.d(TAG, "onBindViewHolder(" + position + ", video)");
            ((VideoViewHolder) holder).bind((Video) media);
        }

        if (holder instanceof ImageViewHolder && media instanceof Image) {
            Log.d(TAG, "onBindViewHolder(" + position + ", image)");
            ((ImageViewHolder) holder).bind((Image) media);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.size() == 0) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        if (medias == null) {
            throw new IllegalStateException();
        }

        Media media = medias.get(position);

        if (payloads.contains(KEY_PREVIOUS_SELECTED_POSITION)) {
            if (holder instanceof VideoViewHolder && media instanceof Video) {
                Log.d(TAG, "onBindViewHolder(" + position + ", video) PREVIOUS_SELECTED_POSITION");
                ((VideoViewHolder) holder).release();
            } else if (holder instanceof ImageViewHolder && media instanceof Image) {
                Log.d(TAG, "onBindViewHolder(" + position + ", image) PREVIOUS_SELECTED_POSITION");
            }
        }

        if (payloads.contains(KEY_NEW_SELECTED_POSITION)) {
            mediaPlayer.reset();
            if (holder instanceof VideoViewHolder && media instanceof Video) {
                Log.d(TAG, "onBindViewHolder(" + position + ", video) NEW_SELECTED_POSITION");
                ((VideoViewHolder) holder).play((Video) media);
            } else if (holder instanceof ImageViewHolder && media instanceof Image) {
                Log.d(TAG, "onBindViewHolder(" + position + ", image) NEW_SELECTED_POSITION");
            }
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
        @BindView(R.id.photo_view)
        PhotoView photoView;

        ImageViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(@NonNull Image image) {
            Glide.with(photoView.getContext())
                    .load(image.getUrl())
                    .apply(new RequestOptions()
                                   .fitCenter()
                                   .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(photoView);
        }
    }

    static final class VideoViewHolder extends RecyclerView.ViewHolder
            implements MediaPlayer.OnPreparedListener, TextureView.SurfaceTextureListener {

        @BindView(R.id.root_view)
        View rootView;
        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.video_view)
        TextureView videoView;
        @Nullable
        private SurfaceTexture surfaceTexture;

        @NonNull
        private final MediaPlayer mediaPlayer;
        @NonNull
        private final MediaPlayerHolder mediaPlayerHolder;
        @NonNull
        private final MediaController mediaController;

        @Nullable
        private Video video;
        private boolean thumbnailShown;
        private boolean playAfterSurfaceTextureAvailable;
        private boolean mustStartWhenMediaPlayerIsPrepared;

        VideoViewHolder(@NonNull View view, @NonNull MediaPlayer mediaPlayer) {
            super(view);
            ButterKnife.bind(this, view);
            videoView.setSurfaceTextureListener(this);

            this.mediaPlayer = mediaPlayer;
            mediaPlayerHolder = new MediaPlayerHolder(mediaPlayer);

            mediaController = new MediaController(rootView.getContext());
            mediaController.setMediaPlayer(mediaPlayerHolder);
            mediaController.setAnchorView(videoView);
        }

        void bind(@NonNull Video video) {
            this.video = video;
            release();
            Glide.with(videoView.getContext())
                    .asBitmap()
                    .load(video.getUrl())
                    .apply(new RequestOptions()
                                   .fitCenter()
                                   .frame(0)
                                   .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(thumbnail);
        }

        void play(@NonNull Video video) {
            this.video = video;
            if (surfaceTexture != null) {
                playInner();
            } else {
                playAfterSurfaceTextureAvailable = true;
            }
        }

        void release() {
            videoView.setAlpha(0.0f);
            playAfterSurfaceTextureAvailable = false;
            mustStartWhenMediaPlayerIsPrepared = false;
            mediaController.setEnabled(false);
        }

        private void playInner() {
            Log.d(TAG, "playInner");
            playAfterSurfaceTextureAvailable = false;
            if (video == null || surfaceTexture == null) {
                throw new IllegalStateException();
            }

            try {
                mediaPlayer.setDataSource(video.getUrl());
                mediaPlayer.setSurface(new Surface(surfaceTexture));
                mediaPlayer.setOnPreparedListener(this);

                mustStartWhenMediaPlayerIsPrepared = true;
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                Log.d(TAG, "mediaPlayer exception");
                mustStartWhenMediaPlayerIsPrepared = false;
                mediaPlayer.reset();
            }
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Log.d(TAG, "onPrepared");
            if (mustStartWhenMediaPlayerIsPrepared) {
                videoView.setAlpha(1.0f);
                TextureViewHelper.adjustAspectRatio(videoView,
                                                    mediaPlayer.getVideoWidth(),
                                                    mediaPlayer.getVideoHeight());
                mediaPlayer.start();
                mediaController.setEnabled(true);
            }
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureAvailable");
            surfaceTexture = surface;
            if (playAfterSurfaceTextureAvailable) {
                playInner();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    }


    enum ViewType {
        VIDEO(R.layout.item_fullscreen_video),
        IMAGE(R.layout.item_fullscreen_image);

        int resourceId;

        ViewType(int resourceId) {
            this.resourceId = resourceId;
        }
    }
}
