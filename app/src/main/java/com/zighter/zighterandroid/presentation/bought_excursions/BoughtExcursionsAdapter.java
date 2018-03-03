package com.zighter.zighterandroid.presentation.bought_excursions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursion;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static com.zighter.zighterandroid.presentation.bought_excursions.BoughtExcursionsAdapter.BoughtExcursionViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoughtExcursionsAdapter extends RecyclerView.Adapter<BoughtExcursionViewHolder> {

    @Nullable
    private final OnBoughtExcursionClickListener onBoughtExcursionClickListener;
    @Nullable
    private List<BoughtExcursion> excursions;
    @Nullable
    private List<DownloadStatus> downloadStatuses;

    BoughtExcursionsAdapter(@Nullable OnBoughtExcursionClickListener onBoughtExcursionClickListener) {
        this.onBoughtExcursionClickListener = onBoughtExcursionClickListener;
    }

    void setExcursions(@NonNull List<BoughtExcursion> excursions) {
        this.excursions = excursions;
        downloadStatuses = new ArrayList<>(excursions.size());
        for (BoughtExcursion boughtExcursion : excursions) {
            downloadStatuses.add(DownloadStatus.IDLE);
        }
    }

    @Override
    public BoughtExcursionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bought_excursion,
                                                                     parent,
                                                                     false);
        return new BoughtExcursionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoughtExcursionViewHolder holder, int position) {
        if (excursions == null || downloadStatuses == null) throw new IllegalStateException();

        holder.bind(excursions.get(position), downloadStatuses.get(position));

        holder.setOnClickListener(view -> {
            if (excursions == null || downloadStatuses == null) throw new IllegalStateException();

            if (onBoughtExcursionClickListener != null) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != NO_POSITION) {
                    BoughtExcursion excursion = excursions.get(adapterPosition);
                    onBoughtExcursionClickListener.onBoughtExcursionClicked(excursion);
                }
            }
        });

        holder.setOnDownloadStatusClickListener(view -> {
            if (excursions == null || downloadStatuses == null) throw new IllegalStateException();

            if (onBoughtExcursionClickListener != null) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != NO_POSITION) {
                    BoughtExcursion excursion = excursions.get(adapterPosition);
                    DownloadStatus downloadStatus = downloadStatuses.get(adapterPosition);
                    if (downloadStatus == DownloadStatus.IDLE) {
                        onBoughtExcursionClickListener.onDownloadClicked(excursion);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return excursions == null ? 0 : excursions.size();
    }

    static class BoughtExcursionViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.owner)
        TextView owner;
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.download_status)
        ImageView downloadStatus;

        BoughtExcursionViewHolder(@NonNull View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, rootView);
        }

        void bind(@NonNull BoughtExcursion boughtExcursion, @NonNull DownloadStatus status) {
            name.setText(boughtExcursion.getName());
            owner.setText(boughtExcursion.getOwner());
            location.setText(boughtExcursion.getLocation());
        }

        void setOnClickListener(@NonNull View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }

        void setOnDownloadStatusClickListener(@NonNull View.OnClickListener listener) {
            downloadStatus.setOnClickListener(listener);
        }
    }

    public interface OnBoughtExcursionClickListener {
        void onBoughtExcursionClicked(@NonNull BoughtExcursion boughtExcursion);

        void onDownloadClicked(@NonNull BoughtExcursion boughtExcursion);
    }

    private enum DownloadStatus {
        DOWNLOADED,
        DOWNLOADING,
        IDLE
    }
}
