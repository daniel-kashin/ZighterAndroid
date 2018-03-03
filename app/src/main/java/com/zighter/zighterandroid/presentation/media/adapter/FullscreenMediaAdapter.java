package com.zighter.zighterandroid.presentation.media.adapter;

import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Video;

import android.widget.CustomMediaController;

import com.zighter.zighterandroid.util.media.MediaPlayerHolder;
import com.zighter.zighterandroid.util.media.TextureViewHelper;

import java.io.IOException;
import java.util.ArrayList;
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
    private final OnFullscreenMediaClickListener onClickListener;
    @Nullable
    private final OnMediaUploadListener onUploadListener;

    @NonNull
    private final MediaPlayer mediaPlayer;
    @NonNull
    private final CustomMediaController mediaController;
    private int currentSelectedPosition;
    @Nullable
    private List<UploadState> uploadStates;

    FullscreenMediaAdapter(@Nullable OnFullscreenMediaClickListener onClickListener,
                           @Nullable OnMediaUploadListener onUploadListener,
                           @NonNull ViewGroup mediaControllerView) {
        this.onClickListener = onClickListener;
        this.onUploadListener = onUploadListener;

        mediaPlayer = new MediaPlayer();

        mediaController = new CustomMediaController(mediaControllerView.getContext());
        mediaController.setMediaPlayer(new MediaPlayerHolder(mediaPlayer));
        mediaController.setAnchorView(mediaControllerView);

        currentSelectedPosition = NO_POSITION;
    }

    @Override
    void setMedias(@NonNull List<DrawableMedia> medias) {
        int size = medias.size();

        uploadStates = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            uploadStates.add(UploadState.NOT_STARTED);
        }
        super.setMedias(medias);
    }

    void setCurrentSelectedPosition(int position) {
        Log.d(TAG, "setCurrentSelectedPosition(" + position + ")");
        if (uploadStates == null) {
            throw new IllegalStateException();
        }

        if (currentSelectedPosition != position) {
            int oldPosition = currentSelectedPosition;
            currentSelectedPosition = position;

            mediaController.doHide();
            mediaPlayer.reset();

            if (onUploadListener != null) {
                UploadState uploadState = uploadStates.get(position);
                if (uploadState == UploadState.STARTED) {
                    onUploadListener.onMediaUploadStarted(currentSelectedPosition);
                } else if (uploadState == UploadState.FAILED) {
                    onUploadListener.onMediaUploadFailed(currentSelectedPosition);
                } else if (uploadState == UploadState.SUCCESS) {
                    onUploadListener.onMediaUploadSuccess(currentSelectedPosition);
                }
            }

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

    private void setUploadState(int position, @NonNull UploadState uploadState) {
        if (uploadStates == null) {
            throw new IllegalStateException();
        }
        uploadStates.set(position, uploadState);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int resourceId) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resourceId, parent, false);

        View.OnClickListener onClickListener = it -> {
            if (this.onClickListener != null) {
                this.onClickListener.onFullscreenMediaClicked();
            }
        };

        OnMediaUploadListener onUploadListener = new OnMediaUploadListener() {
            @Override
            public void onMediaUploadStarted(int adapterPosition) {
                if (adapterPosition != NO_POSITION) {
                    setUploadState(adapterPosition, UploadState.STARTED);

                    OnMediaUploadListener outerListener = FullscreenMediaAdapter.this.onUploadListener;
                    if (outerListener != null && adapterPosition == currentSelectedPosition) {
                        outerListener.onMediaUploadStarted(adapterPosition);
                    }
                }
            }

            @Override
            public void onMediaUploadSuccess(int adapterPosition) {
                if (adapterPosition != NO_POSITION) {
                    setUploadState(adapterPosition, UploadState.SUCCESS);

                    OnMediaUploadListener outerListener = FullscreenMediaAdapter.this.onUploadListener;
                    if (outerListener != null && adapterPosition == currentSelectedPosition) {
                        outerListener.onMediaUploadSuccess(adapterPosition);
                    }
                }
            }

            @Override
            public void onMediaUploadFailed(int adapterPosition) {
                if (adapterPosition != NO_POSITION) {
                    setUploadState(adapterPosition, UploadState.FAILED);

                    OnMediaUploadListener outerListener = FullscreenMediaAdapter.this.onUploadListener;
                    if (outerListener != null && adapterPosition == currentSelectedPosition) {
                        outerListener.onMediaUploadFailed(adapterPosition);
                    }
                }
            }
        };

        if (resourceId == VIDEO.resourceId) {
            VideoViewHolder videoViewHolder = new VideoViewHolder(view,
                                                                  mediaPlayer,
                                                                  mediaController,
                                                                  onUploadListener);
            videoViewHolder.setOnClickListener(onClickListener);
            return videoViewHolder;
        } else if (resourceId == IMAGE.resourceId) {
            ImageViewHolder imageViewHolder = new ImageViewHolder(view, onUploadListener);
            imageViewHolder.setOnClickListener(onClickListener);
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
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        setUploadState(holder.getAdapterPosition(), UploadState.NOT_STARTED);
        super.onViewRecycled(holder);
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

        @NonNull
        private final OnMediaUploadListener onUploadListener;

        ImageViewHolder(@NonNull View view, @NonNull OnMediaUploadListener onUploadListener) {
            super(view);
            ButterKnife.bind(this, view);

            this.onUploadListener = onUploadListener;
        }

        void setOnClickListener(@NonNull View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
            photoView.setOnClickListener(listener);
        }

        void bind(@NonNull Image image) {
            onUploadListener.onMediaUploadStarted(getAdapterPosition());

            RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    onUploadListener.onMediaUploadFailed(getAdapterPosition());
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    onUploadListener.onMediaUploadSuccess(getAdapterPosition());
                    return false;
                }
            };

            Glide.with(photoView.getContext())
                    .load(image.getUrl())
                    .listener(requestListener)
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
        @NonNull
        private final OnMediaUploadListener onUploadListener;

        @Nullable
        private Video video;
        private boolean playInnerAfterSurfaceTextureAvailable;
        private boolean startMediaPlayerWhenPrepared;
        private boolean refreshSurfaceTexture;

        VideoViewHolder(@NonNull View view,
                        @NonNull MediaPlayer mediaPlayer,
                        @NonNull CustomMediaController mediaController,
                        @NonNull OnMediaUploadListener onUploadListener) {
            super(view);
            ButterKnife.bind(this, view);
            videoView.setSurfaceTextureListener(this);

            this.mediaPlayer = mediaPlayer;
            this.mediaController = mediaController;
            this.onUploadListener = onUploadListener;
        }

        void setOnClickListener(@NonNull View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
            thumbnail.setOnClickListener(listener);
            videoView.setOnClickListener(listener);
        }

        void bind(@NonNull Video video) {
            onUploadListener.onMediaUploadStarted(getAdapterPosition());

            this.video = video;
            reset();

            RequestListener<Bitmap> requestListener = new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    onUploadListener.onMediaUploadFailed(getAdapterPosition());
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }
            };

            Glide.with(videoView.getContext())
                    .asBitmap()
                    .load(video.getUrl())
                    .listener(requestListener)
                    .apply(new RequestOptions()
                                   .fitCenter()
                                   .frame(0)
                                   .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(thumbnail);
        }

        void play(@NonNull Video video) {
            this.video = video;
            refreshSurfaceTexture = true;
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
            refreshSurfaceTexture = false;
        }

        private void playInner() {
            Log.d(TAG, "playInner");
            if (video == null || surfaceTexture == null) {
                throw new IllegalStateException();
            }

            playInnerAfterSurfaceTextureAvailable = false;

            try {
                mediaPlayer.reset();
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.setSurface(new Surface(surfaceTexture));

                mediaPlayer.setDataSource(video.getUrl());
                startMediaPlayerWhenPrepared = true;
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                Log.d(TAG, "mediaPlayer exception");
                mediaPlayer.reset();
                reset();
                onUploadListener.onMediaUploadFailed(getAdapterPosition());
            }
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Log.d(TAG, "onPrepared");
            if (startMediaPlayerWhenPrepared) {
                startMediaPlayerWhenPrepared = false;
                videoView.setAlpha(1.0f);
                TextureViewHelper.adjustAspectRatio(videoView,
                                                    mediaPlayer.getVideoWidth(),
                                                    mediaPlayer.getVideoHeight());
                onUploadListener.onMediaUploadSuccess(getAdapterPosition());
                mediaPlayer.start();
                mediaController.show(0);
            }
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d(TAG, "onSurfaceTextureAvailable");
            surfaceTexture = surface;
            if (playInnerAfterSurfaceTextureAvailable) {
                playInner();
            } else if (refreshSurfaceTexture) {
                mediaPlayer.setSurface(new Surface(surfaceTexture));
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

    public interface OnFullscreenMediaClickListener {
        void onFullscreenMediaClicked();
    }

    public interface OnMediaUploadListener {
        void onMediaUploadStarted(int adapterPosition);

        void onMediaUploadSuccess(int adapterPosition);

        void onMediaUploadFailed(int adapterPosition);
    }

    private enum UploadState {
        FAILED,
        SUCCESS,
        NOT_STARTED,
        STARTED
    }
}
