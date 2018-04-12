package com.zighter.zighterandroid.presentation.search;

import android.annotation.SuppressLint;
import android.support.v4.content.ContextCompat;
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
import static com.zighter.zighterandroid.presentation.search.SearchExcursionsAdapter.OnSearchExcursionClickListener.ItemType.GUIDE;
import static com.zighter.zighterandroid.presentation.search.SearchExcursionsAdapter.OnSearchExcursionClickListener.ItemType.MEDIA;
import static com.zighter.zighterandroid.presentation.search.SearchExcursionsAdapter.OnSearchExcursionClickListener.ItemType.ROUTE;

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

        SearchExcursionViewHolder holder = new SearchExcursionViewHolder(view);
        holder.setOnClickListener(onSearchExcursionClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(SearchExcursionViewHolder holder, int position) {
        if (excursions == null) throw new IllegalStateException();

        holder.bind(excursions.get(position), position == 0);
    }

    @Override
    public void onViewRecycled(SearchExcursionViewHolder holder) {
        holder.unbind();
        super.onViewRecycled(holder);
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
        @BindView(R.id.personal_recommendation)
        TextView personalRecommendation;
        @Nullable
        ServiceSearchExcursion excursion;

        SearchExcursionViewHolder(@NonNull View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, rootView);
        }

        void bind(@NonNull ServiceSearchExcursion excursion, boolean isRecommendation) {
            this.excursion = excursion;

            name.setText(excursion.getName());
            location.setText(excursion.getLocation());
            owner.setText(excursion.getProviderUsername());

            if (isRecommendation) {
                personalRecommendation.setVisibility(View.VISIBLE);
            } else {
                personalRecommendation.setVisibility(View.GONE);
            }

            String rouble = " " + rootView.getContext().getString(R.string.rouble);
            String free = rootView.getContext().getString(R.string.free);

            showPrice(free, rouble, excursion.getRoutePrice(), layoutMap, priceMap, isRecommendation);
            showPrice(free, rouble, excursion.getMediaPrice(), layoutMedia, priceMedia, isRecommendation);
            showPrice(free, rouble, excursion.getGuidePrice(), layoutGuide, priceGuide, isRecommendation);

            Glide.with(imageView.getContext())
                    .load(excursion.getImageUrl())
                    .apply(new RequestOptions()
                                   .circleCrop()
                                   .diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(imageView);
        }

        void unbind() {
            this.excursion = null;
        }

        @SuppressLint("SetTextI18n")
        private static void showPrice(@NonNull String free,
                                      @NonNull String rouble,
                                      @Nullable Double price,
                                      @NonNull RelativeLayout layout,
                                      @NonNull TextView priceText,
                                      boolean isRecommendation) {
            int priceTextColor = isRecommendation ? R.color.zighterPrimary : R.color.secondaryText;
            priceText.setTextColor(ContextCompat.getColor(priceText.getContext(), priceTextColor));

            if (price != null) {
                if (price == 0.0) {
                    priceText.setText(free);
                } else {
                    priceText.setText(price.intValue() + rouble);
                }
                layout.setAlpha(1.0f);
            } else {
                priceText.setText(null);
                layout.setAlpha(0.4f);
            }
        }

        void setOnClickListener(@Nullable OnSearchExcursionClickListener listener) {
            if (listener == null) {
                priceMap.setOnClickListener(null);
                priceGuide.setOnClickListener(null);
                priceMedia.setOnClickListener(null);
            } else {
                priceMap.setOnClickListener(view -> {
                    if (excursion == null) return;
                    listener.onBuyExcursionClicked(ROUTE, excursion.getUuid(), excursion.getRoutePrice());
                });
                priceGuide.setOnClickListener(view -> {
                    if (excursion == null) return;
                    listener.onBuyExcursionClicked(GUIDE, excursion.getUuid(), excursion.getRoutePrice());
                });
                priceMedia.setOnClickListener(view -> {
                    if (excursion == null) return;
                    listener.onBuyExcursionClicked(MEDIA, excursion.getUuid(), excursion.getRoutePrice());
                });
            }
        }
    }

    public interface OnSearchExcursionClickListener {
        void onBuyExcursionClicked(@NonNull ItemType itemType,
                                   @NonNull String excursionUuid,
                                   @Nullable Double price);

        enum ItemType {
            ROUTE,
            MEDIA,
            GUIDE
        }
    }
}
