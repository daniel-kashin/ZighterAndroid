package com.zighter.zighterandroid.presentation.excursion.sight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.media.Media;
import com.zighter.zighterandroid.data.entities.media.Video;
import com.zighter.zighterandroid.util.ImageHelper;

import java.util.List;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;
import static com.zighter.zighterandroid.presentation.excursion.sight.ThumbnailsMediaAdapter.ViewType.IMAGE;
import static com.zighter.zighterandroid.presentation.excursion.sight.ThumbnailsMediaAdapter.ViewType.VIDEO;

public class ThumbnailsMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final MultiTransformation<Bitmap> HOLDER_TRANSFORMATION =
            new MultiTransformation<>(new CenterCrop(), new RoundedCornersTransformation(14, 0));
    private static final String TAG = "ThumbnailsMediaAdapter";

    @Nullable
    private List<Media> medias;

    ThumbnailsMediaAdapter() {
    }

    void setMedias(@NonNull List<Media> medias) {
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

        Media media = medias.get(position);
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

            Glide.with(itemView.getContext())
                    .load(image.getUrl())
                    .apply(bitmapTransform(HOLDER_TRANSFORMATION))
                    .into(imageView);
        }
    }

    static final class VideoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_view)
        View rootView;
        @BindView(R.id.image_view)
        ImageView imageView;

        @Nullable
        Disposable loadVideoDisposable;

        VideoViewHolder(@NonNull View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(@NonNull Video video) {
            if (loadVideoDisposable != null) {
                loadVideoDisposable.dispose();
                loadVideoDisposable = null;
            }

            loadVideoDisposable = Single
                    .fromCallable(() -> ImageHelper.getBitmapFromUri(video.getUrl()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                        Glide.with(imageView.getContext())
                                .asBitmap()
                                .load(bitmap)
                                .apply(new RequestOptions().transform(HOLDER_TRANSFORMATION))
                                .into(imageView);
                    }, throwable -> {
                        // do nothing
                    });
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
