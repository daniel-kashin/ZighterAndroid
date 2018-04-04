package com.zighter.zighterandroid.presentation.search;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.media.Image;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchExcursion;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus.DownloadStatus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchExcursionsAdapter extends RecyclerView.Adapter<SearchExcursionsAdapter.SearchExcursionViewHolder> {

    @Nullable
    private final OnSearchExcursionClickListener onSearchExcursionClickListener;
    @Nullable
    private List<ServiceSearchExcursion> excursions;

    SearchExcursionsAdapter(@Nullable OnSearchExcursionClickListener onSearchExcursionClickListener) {
        this.onSearchExcursionClickListener = onSearchExcursionClickListener;
    }

    void setExcursions(@NonNull List<ServiceSearchExcursion> excursions) {
        this.excursions = excursions;
    }

    @Override
    public SearchExcursionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_excursion,
                                                                     parent,
                                                                     false);
        return new SearchExcursionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchExcursionViewHolder holder, int position) {
        if (excursions == null) throw new IllegalStateException();

        holder.bind(excursions.get(position));

        holder.setOnClickListener(view -> {
            if (excursions == null) throw new IllegalStateException();

            if (onSearchExcursionClickListener != null) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != NO_POSITION) {
                    ServiceSearchExcursion excursion = excursions.get(adapterPosition);
                    onSearchExcursionClickListener.onSearchExcursionClicked(excursion);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return excursions == null ? 0 : excursions.size();
    }

    static class SearchExcursionViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.owner)
        TextView owner;
        @BindView(R.id.location)
        TextView location;
        @BindView(R.id.image)
        ImageView imageView;
        @BindView(R.id.layout_guide)
        RelativeLayout layoutGuide;
        @BindView(R.id.layout_map)
        RelativeLayout layoutMap;
        @BindView(R.id.layout_media)
        RelativeLayout layoutMedia;
        @BindView(R.id.price_guide)
        TextView priceGuide;
        @BindView(R.id.price_map)
        TextView priceMap;
        @BindView(R.id.price_media)
        TextView priceMedia;

        SearchExcursionViewHolder(@NonNull View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, rootView);
        }

        void bind(@NonNull ServiceSearchExcursion excursion) {
            name.setText(excursion.getName());
            location.setText(excursion.getLocation());
            owner.setText(excursion.getProviderUsername());

            String rouble = " " + rootView.getContext().getString(R.string.rouble);
            String free = rootView.getContext().getString(R.string.free);

            showPrice(free, rouble, excursion.getRoutePrice(), layoutMap, priceMap);
            showPrice(free, rouble, excursion.getRoutePrice(), layoutMedia, priceMedia);
            showPrice(free, rouble, excursion.getRoutePrice(), layoutGuide, priceGuide);

            Glide.with(imageView.getContext())
                    .load(excursion.getImageUrl())
                    .apply(new RequestOptions()
                                   .circleCrop()
                                   .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(imageView);
        }

        @SuppressLint("SetTextI18n")
        private static void showPrice(@NonNull String free,
                                      @NonNull String rouble,
                                      @Nullable Double price,
                                      @NonNull RelativeLayout layout,
                                      @NonNull TextView priceText) {
            if (price != null) {
                if (price == 0.0) {
                    priceText.setText(free);
                } else {
                    priceText.setText(price.intValue() + rouble);
                }
            } else {
                priceText.setText(null);
                layout.setAlpha(0.4f);
            }
        }

        void setOnClickListener(@NonNull View.OnClickListener listener) {
            rootView.setOnClickListener(listener);
        }
    }

    public interface OnSearchExcursionClickListener {
        void onSearchExcursionClicked(@NonNull ServiceSearchExcursion excursion);
    }
}
