package com.zighter.zighterandroid.presentation.excursion.map;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.service.Excursion;
import com.zighter.zighterandroid.data.entities.service.Path;
import com.zighter.zighterandroid.data.entities.service.Point;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.data.location.LocationListenerHolder;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.excursion.LocationPermissionListener;
import com.zighter.zighterandroid.presentation.excursion.holder.ExcursionHolderActivity;
import com.zighter.zighterandroid.util.IconHelper;
import com.zighter.zighterandroid.util.IconHelperType;
import com.zighter.zighterandroid.util.LocationSettingsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

@SuppressWarnings("CodeBlock2Expr")
public class ExcursionMapFragment extends BaseSupportFragment implements ExcursionMapView,
        LocationListenerHolder.OnLocationChangeListener, LocationPermissionListener {
    private static final String TAG = "ExcursionMapFragment";

    @InjectPresenter
    ExcursionMapPresenter presenter;

    @ProvidePresenter
    public ExcursionMapPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Inject
    Provider<ExcursionMapPresenter> presenterProvider;
    @Inject
    LocationListenerHolder locationListenerHolder;

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
    @BindView(R.id.location_image_view)
    ImageView locationImageView;

    @NonNull
    private HashMap<Marker, Sight> markersToSights = new HashMap<>();
    @Nullable
    private Marker selectedMarker;
    @Nullable
    private Marker currentLocationMarker;

    private boolean onDestroyViewCalled = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_excursion_map;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof ExcursionHolderActivity)) {
            throw new IllegalStateException("Parent activity must me ExcursionHolderActivity");
        }
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
        locationImageView.setVisibility(View.INVISIBLE);
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
        locationListenerHolder.unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onDestroyViewCalled = false;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        onDestroyViewCalled = true;
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
        outState.clear();
        map.onSaveInstanceState(outState);
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
    public void onLocationProvidersAvailabilityChanged(boolean networkProviderEnabled, boolean gpsProviderEnabled) {
        presenter.onLocationProvidersAvailabilityChanged(networkProviderEnabled, gpsProviderEnabled);
    }


    @Override
    public void showCurrentLocation(@NonNull Location location) {
        map.getMapAsync(mapboxMap -> {
            if (onDestroyViewCalled) return;

            if (currentLocationMarker != null) mapboxMap.removeMarker(currentLocationMarker);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .icon(IconHelper.getIcon(getContext(), IconHelperType.CURRENT_LOCATION));
            currentLocationMarker = mapboxMap.addMarker(markerOptions);
        });
    }

    @Override
    public void updateLocationAvailability(boolean isPermissionAvailable, boolean isLocationProviderEnabled) {
        locationImageView.setVisibility(View.VISIBLE);
        if (!isPermissionAvailable) {
            locationImageView.setOnClickListener(view -> {
                LocationSettingsHelper.requestOpenPermissionSettings(getActivity());
            });
            removeCurrentLocationMarker();
        } else {
            if (isLocationProviderEnabled) {
                locationImageView.setOnClickListener(view -> {
                    if (currentLocationMarker != null) {
                        map.getMapAsync(mapboxMap -> {
                            if (onDestroyViewCalled) return;

                            mapboxMap.animateCamera(mapboxMapInner -> new CameraPosition.Builder()
                                                            .target(currentLocationMarker.getPosition())
                                                            .build(),
                                                    300);
                        });
                    }
                });
            } else {
                locationImageView.setOnClickListener(view -> {
                    LocationSettingsHelper.requestOpenGpsSettings(getActivity());
                });
                removeCurrentLocationMarker();
            }
        }
    }


    @Override
    public void showLoading() {
        map.getMapAsync(mapboxMap -> {
            if (onDestroyViewCalled) return;

            mapboxMap.setOnMarkerClickListener(null);
            progressBar.setVisibility(View.VISIBLE);
        });
    }

    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showExcursion(@NonNull final Excursion excursion) {
        map.getMapAsync(mapboxMap -> {
            if (onDestroyViewCalled) return;

            // add sights
            markersToSights.clear();
            for (int i = 0; i < excursion.getSightSize(); ++i) {
                if (onDestroyViewCalled) return;

                Sight sight = excursion.getSightAt(i);
                Marker marker = mapboxMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(sight.getLatitude(), sight.getLongitude()))
                                                            .title(sight.getName()));
                markersToSights.put(marker, sight);
            }

            // add paths
            for (int i = 0; i < excursion.getPathSize(); ++i) {
                if (onDestroyViewCalled) return;

                Path path = excursion.getPathAt(i);

                Point firstEndpoint = path.getFirstEndpoint();
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(firstEndpoint.getLatitude(),
                                                                            firstEndpoint.getLongitude())));
                Point secondEndpoint = path.getSecondEndpoint();
                mapboxMap.addMarker(new MarkerOptions().position(new LatLng(secondEndpoint.getLatitude(),
                                                                            secondEndpoint.getLongitude())));

                List<LatLng> points = new ArrayList<>();
                for (int j = 0; j < path.getPointSize(); ++j) {
                    Point point = path.getPointAt(j);
                    points.add(new LatLng(point.getLatitude(), point.getLongitude()));
                }

                mapboxMap.addPolyline(new PolylineOptions()
                                              .addAll(points)
                                              .color(Color.parseColor("#222222"))
                                              .alpha(0.9f)
                                              .width(3f));
            }

            mapboxMap.moveCamera(mapboxMapInner -> new CameraPosition.Builder()
                    .target(new LatLng(excursion.getSightAt(0).getLatitude(),
                                       excursion.getSightAt(0).getLongitude()))
                    .zoom(13)
                    .build()
            );
            mapboxMap.setMaxZoomPreference(25);
            mapboxMap.setMinZoomPreference(10);

            if (onDestroyViewCalled) return;

            mapboxMap.setOnMarkerClickListener(marker -> {
                if (onDestroyViewCalled) return false;

                if (marker != selectedMarker && markersToSights.containsKey(marker)) {
                    ExcursionHolderActivity activity = (ExcursionHolderActivity) getActivity();
                    if (activity != null && activity.onSightSelected(markersToSights.get(marker))) {
                        presenter.onSightClicked(markersToSights.get(marker), marker);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            });

            hideLoading();
            map.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showNetworkUnavailable() {
        hideLoading();
    }

    @Override
    public void showServerException() {
        hideLoading();
    }

    @Override
    public void showSightSelection(@Nullable Sight sight, @Nullable Marker marker) {
        map.getMapAsync(mapboxMap -> {
            if (onDestroyViewCalled) return;

            Marker previous = selectedMarker;
            if (previous != null) {
                previous.setIcon(IconFactory.getInstance(getContext()).defaultMarker());
                mapboxMap.updateMarker(previous);
            }
            if (sight == null || marker == null) {
                // remove marker selection
                selectedMarker = null;
            } else {
                // set marker selection
                selectedMarker = marker;

                selectedMarker.setIcon(IconHelper.getIcon(getContext(), IconHelperType.CHECKED_SIGHT));
                mapboxMap.updateMarker(selectedMarker);
            }
        });
    }

    public void removeSightSelection() {
        presenter.onSightClicked(null, selectedMarker);
    }

    private void removeCurrentLocationMarker() {
        if (currentLocationMarker != null) {
            map.getMapAsync(mapboxMap -> {
                if (onDestroyViewCalled) return;

                if (currentLocationMarker != null) {
                    currentLocationMarker.setIcon(IconHelper.getIcon(getContext(), IconHelperType.CURRENT_LOCATION_OFFLINE));
                    mapboxMap.updateMarker(currentLocationMarker);
                }
            });
        }
    }
}
