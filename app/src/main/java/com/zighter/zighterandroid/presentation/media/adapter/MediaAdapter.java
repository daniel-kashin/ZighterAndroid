package com.zighter.zighterandroid.presentation.media.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.zighter.zighterandroid.data.entities.media.DrawableMedia;

import java.util.List;

public abstract class MediaAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    @Nullable
    private List<DrawableMedia> medias;

    void setMedias(@NonNull List<DrawableMedia> medias) {
        this.medias = medias;
    }

    @NonNull
    DrawableMedia getItemAt(int position) {
        if (medias == null) {
            throw new IllegalStateException();
        }
        return medias.get(position);
    }

    @Override
    public int getItemCount() {
        return medias != null ? medias.size() : 0;
    }
}
