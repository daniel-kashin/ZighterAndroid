package com.zighter.zighterandroid.presentation.bought_excursions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.excursion.BoughtExcursionWithStatus;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static com.zighter.zighterandroid.presentation.bought_excursions.BoughtExcursionsAdapter.BoughtExcursionViewHolder;
import static com.zighter.zighterandroid.data.entities.excursion.BoughtExcursionWithStatus.DownloadStatus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoughtExcursionsAdapter extends RecyclerView.Adapter<BoughtExcursionViewHolder> {

    @Nullable
    private final OnBoughtExcursionClickListener onBoughtExcursionClickListener;
    @Nullable
    private List<BoughtExcursionWithStatus> excursions;

    BoughtExcursionsAdapter(@Nullable OnBoughtExcursionClickListener onBoughtExcursionClickListener) {
        this.onBoughtExcursionClickListener = onBoughtExcursionClickListener;
    }

    void setExcursions(@NonNull List<BoughtExcursionWithStatus> excursions) {
        this.excursions = excursions;
    }

    int refreshExcursionStatus(@NonNull String boughtExcursionUuid, @NonNull DownloadStatus downloadStatus) {
        if (excursions == null) {
            return NO_POSITION;
        }

        int size = excursions.size();
        for (int i = 0; i < size; ++i) {
            BoughtExcursionWithStatus boughtExcursionWithStatus = excursions.get(i);
            if (boughtExcursionWithStatus.getExcursion().getUuid().equals(boughtExcursionUuid)) {
                boughtExcursionWithStatus.setDownloadStatus(downloadStatus);
                return i;
            }
        }

        return NO_POSITION;
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
        if (excursions == null) throw new IllegalStateException();

        holder.bind(excursions.get(position));

        holder.setOnClickListener(view -> {
            if (excursions == null) throw new IllegalStateException();

            if (onBoughtExcursionClickListener != null) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != NO_POSITION) {
                    BoughtExcursionWithStatus excursion = excursions.get(adapterPosition);
                    onBoughtExcursionClickListener.onBoughtExcursionClicked(excursion);
                }
            }
        });

        holder.setOnDownloadStatusClickListener(view -> {
            if (excursions == null) throw new IllegalStateException();

            if (onBoughtExcursionClickListener != null) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != NO_POSITION) {
                    BoughtExcursionWithStatus excursion = excursions.get(adapterPosition);
                    onBoughtExcursionClickListener.onDownloadClicked(excursion);
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
        ImageView downloadStatusImage;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        BoughtExcursionViewHolder(@NonNull View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, rootView);
        }

        void bind(@NonNull BoughtExcursionWithStatus boughtExcursionWithStatus) {
            DownloadStatus downloadStatus = boughtExcursionWithStatus.getStatus();
            if (downloadStatus == DownloadStatus.IDLE) {
                progressBar.setVisibility(View.INVISIBLE);
                downloadStatusImage.setBackgroundResource(R.drawable.ic_arrow_download);
                downloadStatusImage.setVisibility(View.VISIBLE);
            } else if (downloadStatus == DownloadStatus.DOWNLOADING) {
                downloadStatusImage.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            } else if (downloadStatus == DownloadStatus.DOWNLOADED) {
                progressBar.setVisibility(View.INVISIBLE);
                downloadStatusImage.setBackgroundResource(R.drawable.ic_downloaded);
                downloadStatusImage.setVisibility(View.VISIBLE);
            }

            name.setText(boughtExcursionWithStatus.getExcursion().getName());
            owner.setText(boughtExcursionWithStatus.getExcursion().getOwner());
            location.setText(boughtExcursionWithStatus.getExcursion().getLocation());
        }

        void setOnClickListener(@NonNull View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }

        void setOnDownloadStatusClickListener(@NonNull View.OnClickListener listener) {
            downloadStatusImage.setOnClickListener(listener);
            progressBar.setOnClickListener(listener);
        }
    }

    public interface OnBoughtExcursionClickListener {
        void onBoughtExcursionClicked(@NonNull BoughtExcursionWithStatus boughtExcursionWithStatus);

        void onDownloadClicked(@NonNull BoughtExcursionWithStatus boughtExcursionWithStatus);
    }
}
