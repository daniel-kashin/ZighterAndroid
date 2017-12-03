package com.zighter.zighterandroid.presentation.excursion.sight;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zighter.zighterandroid.data.entities.presentation.Media;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final List<Media> mediaList;

    public MediaAdapter(List<Media> mediaList) {
        this.mediaList = new ArrayList<>();
        this.mediaList.addAll(mediaList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }
}
