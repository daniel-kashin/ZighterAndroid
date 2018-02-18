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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Video;
import android.widget.CustomMediaController;
import com.zighter.zighterandroid.util.media.MediaPlayerHolder;
import com.zighter.zighterandroid.util.media.TextureViewHelper;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static com.zighter.zighterandroid.presentation.media.adapter.FullscreenMediaAdapter.ViewType.IMAGE;
import static com.zighter.zighterandroid.presentation.media.adapter.FullscreenMediaAdapter.ViewType.VIDEO;

public class FullscreenMediaAdapter extends MediaAdapter<RecyclerView.ViewHolder> {
    private static final String TAG = "FullscreenMediaAdapter";
    private static final int KEY_NEW_SELECTED_POSITION = 111;

    @Nullable
    private final OnClickListener onClickListener;

    @NonNull
    private final MediaPlayer mediaPlayer;
    @NonNull
    private final CustomMediaController mediaController;
    private int currentSelectedPosition;

    FullscreenMediaAdapter(@Nullable OnClickListener onClickListener, @NonNull ViewGroup mediaControllerView) {
        this.onClickListener = onClickListener;

        mediaPlayer = new MediaPlayer();

        mediaController = new CustomMediaController(mediaControllerView.getContext());
        mediaController.setAnchorView(mediaControllerView);
        mediaController.setMediaPlayer(new MediaPlayerHolder(mediaPlayer));

        currentSelectedPosition = NO_POSITION;
    }

    void setCurrentSelectedPosition(int position) {
        Log.d(TAG, "setCurrentSelectedPosition(" + position + ")");
        if (currentSelectedPosition != position) {
            int oldPosition = currentSelectedPosition;
            currentSelectedPosition = position;

            mediaController.doHide();
            mediaPlayer.reset();

            if (oldPosition != NO_POSITION && oldPosition >= 0 && oldPosition < getItemCount()) {
                notifyItemChanged(oldPosition);
            }

            if (currentSelectedPosition != NO_POSITION && currentSelectedPosition >= 0 && currentSelectedPosition < getItemCount()) {
                notifyItemChanged(currentSelectedPosition, KEY_NEW_SELECTED_POSITION);
            }
        }
    }

    void onStart() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    void onPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    void onDestroy() {
        mediaPlayer.release();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int resourceId) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false);

        View.OnClickListener listener = it -> {
            if (onClickListener != null) {
                onClickListener.onFullscreenMediaClicked();
            }
        };

        if (resourceId == VIDEO.resourceId) {
            VideoViewHolder videoViewHolder = new VideoViewHolder(view, mediaPlayer, mediaController);
            videoViewHolder.setOnClickListener(listener);
            return videoViewHolder;
        } else if (resourceId == IMAGE.resourceId) {
            ImageViewHolder imageViewHolder = new ImageViewHolder(view);
            imageViewHolder.setOnClickListener(listener);
            return imageViewHolder;
        }

        throw new IllegalStateException();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DrawableMedia media = getItemAt(position);

        if (holder instanceof VideoViewHolder && media instanceof Video) {
            Log.d(TAG, "onBindViewHolder(" + position + ", video)");
            ((VideoViewHolder) holder).bind((Video) media);
            if (position == currentSelectedPosition) {
                ((VideoViewHolder) holder).play((Video) media);
            }
        }

        if (holder instanceof ImageViewHolder && media instanceof Image) {
            Log.d(TAG, "onBindViewHolder(" + position + ", image)");
            ((ImageViewHolder) holder).bind((Image) media);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads.size() == 0) {
            super.onBindViewHolder(holder, position, payloads);
            return;
        }

        DrawableMedia media = getItemAt(position);

        if (payloads.contains(KEY_NEW_SELECTED_POSITION)) {
            if (holder instanceof VideoViewHolder && media instanceof Video) {
                Log.d(TAG, "onBindViewHolder(" + position + ", video) NEW_SELECTED_POSITION");
                ((VideoViewHolder) holder).play((Video) media);
            } else if (holder instanceof ImageViewHolder && media instanceof Image) {
                Log.d(TAG, "onBindViewHolder(" + position + ", image) NEW_SELECTED_POSITION");
            }
        }
    }



    @Override
    public int getItemViewType(int position) {
        DrawableMedia media = getItemAt(position);

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

        void setOnClickListener(@NonNull View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
            photoView.setOnClickListener(listener);
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
        private final CustomMediaController mediaController;

        @Nullable
        private Video video;
        private boolean playInnerAfterSurfaceTextureAvailable;
        private boolean startMediaPlayerWhenPrepared;

        VideoViewHolder(@NonNull View view,
                        @NonNull MediaPlayer mediaPlayer,
                        @NonNull CustomMediaController mediaController) {
            super(view);
            ButterKnife.bind(this, view);
            videoView.setSurfaceTextureListener(this);

            this.mediaPlayer = mediaPlayer;
            this.mediaController = mediaController;
        }

        void setOnClickListener(@NonNull View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
            thumbnail.setOnClickListener(listener);
            videoView.setOnClickListener(listener);
        }

        void bind(@NonNull Video video) {
            this.video = video;
            reset();
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
                playInnerAfterSurfaceTextureAvailable = true;
            }
        }

        void reset() {
            videoView.setAlpha(0.0f);
            playInnerAfterSurfaceTextureAvailable = false;
            startMediaPlayerWhenPrepared = false;
        }

        private void playInner() {
            Log.d(TAG, "playInner");
            if (video == null || surfaceTexture == null) {
                throw new IllegalStateException();
            }

            playInnerAfterSurfaceTextureAvailable = false;

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(video.getUrl());
                mediaPlayer.setSurface(new Surface(surfaceTexture));
                mediaPlayer.setOnPreparedListener(this);

                startMediaPlayerWhenPrepared = true;
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Log.d(TAG, "mediaPlayer exception");
                mediaPlayer.reset();
            }
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Log.d(TAG, "onPrepared");
            if (startMediaPlayerWhenPrepared) {
                startMediaPlayerWhenPrepared = false;
                TextureViewHelper.adjustAspectRatio(videoView,
                                                    mediaPlayer.getVideoWidth(),
                                                    mediaPlayer.getVideoHeight());
                mediaPlayer.start();
                mediaController.show(0);
                videoView.setAlpha(1.0f);
            }
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureAvailable");
            surfaceTexture = surface;
            if (playInnerAfterSurfaceTextureAvailable) {
                playInner();
            }
        }

        @Override public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}
        @Override public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }
        @Override public void onSurfaceTextureUpdated(SurfaceTexture surface) {}
    }

    enum ViewType {
        VIDEO(R.layout.item_fullscreen_video),
        IMAGE(R.layout.item_fullscreen_image);

        int resourceId;

        ViewType(int resourceId) {
            this.resourceId = resourceId;
        }
    }

    public interface OnClickListener {
        void onFullscreenMediaClicked();
    }
}
