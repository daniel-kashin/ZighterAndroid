package com.zighter.zighterandroid.presentation.navigation;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ProgressBar;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
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
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(41.38423087823596, 2.177524566650391))
                    .title("1")
            );

            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(41.384319425658035, 2.176108360290528))
                    .title("2")
            );

            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(41.386235240353464, 2.171355485916138))
                    .title("3")
            );

            mapboxMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(41.384037683441974, 2.1787691116333012))
                    .add(new LatLng(41.383953160539086, 2.1786671876907353))
                    .add(new LatLng(41.38450456987822, 2.177921533584595))
                    .add(new LatLng(41.38444017194839, 2.1775835752487187))
                    .add(new LatLng(41.38454079368572, 2.177347540855408))
                    .add(new LatLng(41.384629340685784, 2.176974713802338))
                    .add(new LatLng(41.38481850887286, 2.1763122081756596))
                    .add(new LatLng(41.38489900580681, 2.176129817962647))
                    .add(new LatLng(41.384617266101955, 2.1757650375366215))
                    .color(Color.parseColor("#222222"))
                    .alpha(0.9f)
                    .width(3f)
            );

            mapboxMap.animateCamera(mapboxMapInner -> new CameraPosition.Builder()
                    .target(new LatLng(41.384617266101955, 2.1757650375366215))
                    .zoom(13)
                    .build(), 2000);
            mapboxMap.setMaxZoomPreference(18);
            mapboxMap.setMinZoomPreference(10);

            hideLoading();
            map.setVisibility(View.VISIBLE);
        });
    }

}
