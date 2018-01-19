package com.zighter.zighterandroid.presentation.excursion.sight;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.presentation.Media;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {
    private static final String TAG = "MediaAdapter";

    @NonNull
    private final List<Media> mediaList;

    MediaAdapter(List<Media> mediaList) {
        this.mediaList = new ArrayList<>();
        this.mediaList.addAll(mediaList);
    }

    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);

        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder(" + position + ")");
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    static final class MediaViewHolder extends RecyclerView.ViewHolder {

        private View rootView;

        MediaViewHolder(@NonNull View view) {
            super(view);
            this.rootView = view;
        }
    }
}
