package com.zighter.zighterandroid.presentation.excursion.sight;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.data.location.LocationListenerHolder;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.excursion.LocationPermissionListener;
import com.zighter.zighterandroid.presentation.excursion.holder.ExcursionHolderActivity;
import com.zighter.zighterandroid.util.LocationSettingsHelper;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

public class SightFragment extends BaseSupportFragment implements SightView,
        LocationListenerHolder.OnLocationChangeListener, LocationPermissionListener {
    private static final String KEY_SIGHT = "SIGHT";

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
            throw new IllegalStateException("Sight must be non null when calling providePresenter");
        }

        return sightPresenterBuilderProvider.get()
                .setSight(sight)
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

    @Nullable
    private Sight sight;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sight;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!getArguments().containsKey(KEY_SIGHT) || getArguments().getSerializable(KEY_SIGHT) == null) {
            throw new IllegalStateException("SightFragment must be created using newInstance method");
        }

        sight = (Sight) getArguments().getSerializable(KEY_SIGHT);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
    public void onLocationChanged(@NonNull Location location) {
        presenter.onLocationChanged(location);
    }

    @Override
    public void onLocationProvidersAvailabilityChanged(boolean networkLocationEnabled, boolean gpsLocationEnabled) {
        presenter.onLocationProvidersAvailabilityChanged(networkLocationEnabled, gpsLocationEnabled);
    }

    @Override
    public void updateLocationAvailability(boolean isPermissionAvailable, boolean isLocationProviderEnabled) {
        if (!isPermissionAvailable || !isLocationProviderEnabled) {
            sightDistance.setText("");
        }
    }

    @Override
    public void showSight(@NonNull Sight sight) {
        sightName.setText(sight.getName() != null ? sight.getName() : "Колизей");
        ExcursionHolderActivity activity = (ExcursionHolderActivity) getActivity();
        if (activity != null) {
            activity.onSightShown();
        }
    }

    @Override
    public void showCurrentDistance(int distanceInMeters) {
        String distance = distanceInMeters > 1000
                ? (distanceInMeters / 1000 + " " + getString(R.string.kilometers))
                : (distanceInMeters + " " + getString(R.string.meters));
        sightDistance.setText(distance);
    }
}
