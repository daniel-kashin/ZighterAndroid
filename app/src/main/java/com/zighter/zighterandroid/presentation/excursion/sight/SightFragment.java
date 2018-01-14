package com.zighter.zighterandroid.presentation.excursion.sight;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.excursion.holder.ExcursionHolderActivity;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

public class SightFragment extends BaseSupportFragment implements SightView {
    private static final String KEY_SIGHT = "SIGHT";

    public static SightFragment newInstance(@NonNull Sight sight) {
        SightFragment sightFragment = new SightFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_SIGHT, sight);
        sightFragment.setArguments(arguments);

        return sightFragment;
    }


    @InjectPresenter
    SightPresenter sightPresenter;

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

    @Override
    public void ensureLocationPermissionGranted() {
        int currentLocationPermission = ContextCompat.checkSelfPermission(getContext().getApplicationContext(), SightPresenter.LOCATION_PERMISSION);
        if (currentLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ExcursionHolderActivity activity = (ExcursionHolderActivity) getActivity();
            if (activity != null) {
                activity.requestLocationPermission();
            }
        } else {
            onLocationPermissionEnabled();
        }
    }

    public void onLocationPermissionEnabled() {
        if (sightPresenter != null) {
            sightPresenter.onLocationPermissionEnabled();
        }
    }
}
