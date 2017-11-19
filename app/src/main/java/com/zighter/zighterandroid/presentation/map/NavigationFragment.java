package com.zighter.zighterandroid.presentation.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

public class NavigationFragment extends BaseSupportFragment implements NavigationView {

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }


    @InjectPresenter
    NavigationPresenter navigationPresenter;

    @ProvidePresenter
    public NavigationPresenter providePresenter() {
        return navigationPresenterProvider.get();
    }

    @Inject
    Provider<NavigationPresenter> navigationPresenterProvider;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getNavigationComponent()
                .inject(this);
    }


    @BindView(R.id.map_view)
    MapView map;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_navigation;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews();
        map.onCreate(savedInstanceState);
    }

    private void initializeViews() {
        map.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        map.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        map.onDestroy();
        super.onDestroyView();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }


    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showRoute() {
        map.getMapAsync(mapboxMap -> {
            hideLoading();
            map.setVisibility(View.VISIBLE);
            mapboxMap.animateCamera(mapboxMapInner -> new CameraPosition.Builder()
                    .target(new LatLng(51.50550, -0.07520))
                    .zoom(13)
                    .build(), 1000);
            mapboxMap.setMaxZoomPreference(18);
            mapboxMap.setMinZoomPreference(10);
        });
    }

}
