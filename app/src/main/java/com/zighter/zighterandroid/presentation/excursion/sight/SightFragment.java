package com.zighter.zighterandroid.presentation.excursion.sight;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.bumptech.glide.request.RequestOptions;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.entities.media.DrawableMedia;
import com.zighter.zighterandroid.data.location.LocationListenerHolder;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.excursion.LocationPermissionListener;
import com.zighter.zighterandroid.presentation.media.MediaActivity;
import com.zighter.zighterandroid.util.media.ImageViewLoader;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

import static android.graphics.Typeface.BOLD;

public class SightFragment extends BaseSupportFragment implements SightView,
        LocationListenerHolder.OnLocationChangeListener, LocationPermissionListener {
    private static final String KEY_SIGHT = "MEDIA";

    public static SightFragment newInstance(@NonNull Sight sight) {
        SightFragment sightFragment = new SightFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_SIGHT, sight);
        sightFragment.setArguments(arguments);

        return sightFragment;
    }

    @InjectPresenter
    SightPresenter presenter;

    @ProvidePresenter
    public SightPresenter providePresenter() {
        if (sight == null) {
            throw new IllegalStateException();
        }

        return sightPresenterBuilderProvider.get()
                .sight(sight)
                .build();
    }

    @Inject
    Provider<SightPresenter.Builder> sightPresenterBuilderProvider;
    @Inject
    LocationListenerHolder locationListenerHolder;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getSightComponent()
                .inject(this);
    }

    @BindView(R.id.sight_name)
    TextView sightName;
    @BindView(R.id.sight_distance)
    TextView sightDistance;
    @BindView(R.id.root_view)
    ConstraintLayout rootView;
    @BindView(R.id.sight_description)
    TextView sightDescription;
    @BindView(R.id.media_thumbnail_background)
    ImageView mediaThumbnailBackground;
    @BindView(R.id.media_count)
    TextView mediaCountView;
    @BindView(R.id.media_count_circle)
    ImageView mediaCountCircle;

    @Nullable
    private Sight sight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sight;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initializeSight();
        super.onCreate(savedInstanceState);
    }

    private void initializeSight() {
        if (getArguments() == null || getArguments().getSerializable(KEY_SIGHT) == null) {
            throw new IllegalStateException();
        }
        sight = (Sight) getArguments().getSerializable(KEY_SIGHT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationListenerHolder.unregister(this);
    }

    @Override
    public void onLocationPermissionGranted(boolean granted) {
        if (granted) {
            locationListenerHolder.register(this);
        } else {
            locationListenerHolder.unregister(this);
        }
        presenter.onLocationPermissionGranted(granted);
    }

    @Override
    public void onLocationChanged(@NonNull Location location, boolean active) {
        presenter.onLocationChanged(location, active);
    }

    @Override
    public void onLocationProvidersAvailabilityChanged(boolean networkLocationEnabled, boolean gpsLocationEnabled) {
        presenter.onLocationProvidersAvailabilityChanged(networkLocationEnabled, gpsLocationEnabled);
    }

    @Override
    public void updateLocationAvailability(boolean isPermissionAvailable, boolean isLocationProviderEnabled) {

    }

    @Override
    public void showSight(@NonNull Sight sight, @Nullable ImageViewLoader imageViewLoader) {
        if (getContext() == null) return;

        sightName.setText(sight.getName());
        sightDescription.setText(sight.getDescription());

        int mediaCount = sight.getMediaCount(DrawableMedia.class);
        if (mediaCount == 0) {
            mediaCountView.setVisibility(View.GONE);
            mediaCountCircle.setVisibility(View.GONE);
            mediaThumbnailBackground.setVisibility(View.GONE);
        } else {
            String mediaCountString = String.valueOf(mediaCount);
            SpannableStringBuilder builder = new SpannableStringBuilder(mediaCountString)
                    .append("\n")
                    .append("media");
            builder.setSpan(new StyleSpan(BOLD), 0, mediaCountString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            mediaCountView.setText(builder);

            mediaCountView.setVisibility(View.VISIBLE);
            mediaCountCircle.setVisibility(View.VISIBLE);
            mediaThumbnailBackground.setVisibility(View.VISIBLE);

            Drawable placeholder = new ColorDrawable(getContext().getResources().getColor(R.color.lightHintText));
            if (imageViewLoader != null) {
                imageViewLoader.load(mediaThumbnailBackground,
                                     new RequestOptions()
                                             .centerCrop()
                                             .error(placeholder)
                                             .fallback(placeholder)
                                             .placeholder(placeholder),
                                     null);
            } else {
                mediaThumbnailBackground.setBackground(placeholder);
            }

            mediaThumbnailBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = SightFragment.this.getContext();
                    Sight sight = SightFragment.this.sight;

                    if (context != null) {
                        if (sight == null) {
                            throw new IllegalStateException();
                        }
                        MediaActivity.start(context, sight);
                    }
                }
            });
        }
    }

    @Override
    public void showCurrentDistance(Integer distanceInMeters) {
        if (getContext() != null) {
            if (distanceInMeters == null) {
                sightDistance.setTextColor(ContextCompat.getColor(getContext(), R.color.hintText));
            } else {
                String distance = distanceInMeters > 1000
                        ? distanceInMeters / 1000 + " " + getString(R.string.kilometers)
                        : distanceInMeters + " " + getString(R.string.meters);
                sightDistance.setText(distance);

                sightDistance.setTextColor(ContextCompat.getColor(getContext(), R.color.secondaryText));
            }
        }
    }
}
