package com.zighter.zighterandroid.util;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

public class ImageHelper {
    @Nullable
    public static Bitmap getBitmapFromUri(@NonNull String uri) throws Exception {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(uri, new HashMap<>());
            return retriever.getFrameAtTime();
        } finally {
            retriever.release();
        }
    }
}
