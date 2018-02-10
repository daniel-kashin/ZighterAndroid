package com.zighter.zighterandroid.util;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

public interface ImageViewLoader<T> {

    void load(@NonNull ImageView imageView,
              @NonNull RequestOptions mediaRequestOptions,
              @Nullable RequestListener requestListener);

    class UrlLoader implements ImageViewLoader<String> {
        @NonNull
        private final String url;
        private final boolean asBitmap;

        public UrlLoader(@NonNull String url, boolean asBitmap) {
            this.url = url;
            this.asBitmap = asBitmap;
        }

        @Override
        public void load(@NonNull ImageView imageView,
                         @NonNull RequestOptions mediaRequestOptions,
                         @Nullable RequestListener requestListener) {
            RequestManager requestManager = Glide.with(imageView.getContext());

            RequestBuilder requestBuilder;
            if (asBitmap) {
                requestBuilder = requestManager.asBitmap();
            } else {
                requestBuilder = requestManager.asDrawable();
            }

            requestBuilder.listener(requestListener)
                    .load(url)
                    .apply(mediaRequestOptions)
                    .into(imageView);
        }
    }

}
