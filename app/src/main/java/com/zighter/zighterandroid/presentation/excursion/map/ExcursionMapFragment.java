package com.zighter.zighterandroid.presentation.excursion.map;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

import static com.zighter.zighterandroid.util.IconHelper.Type.CHECKED_SIGHT;

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
        //map.onSaveInstanceState(outState);
    }


    @Override
    public void onLocationPermissionGranted(boolean granted) {
        Log.d(TAG, "onLocationPermissionGranted");
        if (granted) {
            locationListenerHolder.register(this);
        } else {
            locationListenerHolder.unregister(this);
        }
        presenter.onLocationPermissionGranted(granted);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(TAG, "onLocationChanged");
        presenter.onLocationChanged(location);
    }

    @Override
    public void onLocationProvidersAvailabilityChanged(boolean networkProviderEnabled, boolean gpsProviderEnabled) {
        Log.d(TAG, "onLocationProvidersAvailabilityChanged");
        presenter.onLocationProvidersAvailabilityChanged(networkProviderEnabled, gpsProviderEnabled);
    }


    @Override
    public void showCurrentLocation(@NonNull Location location) {
        Toast.makeText(getContext(), "showCurrentLocation", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateLocationAvailability(boolean isPermissionAvailable, boolean isLocationProviderEnabled) {
        locationImageView.setVisibility(View.VISIBLE);
        if (!isPermissionAvailable) {
            locationImageView.setOnClickListener(view -> {
                Toast.makeText(getContext(), "Permission not enabled", Toast.LENGTH_SHORT).show();
            });
        } else {
            if (isLocationProviderEnabled) {
                locationImageView.setOnClickListener(view -> {
                    Toast.makeText(getContext(), "Permission and provider enabled", Toast.LENGTH_SHORT).show();
                });
            } else {
                locationImageView.setOnClickListener(view -> {
                    Toast.makeText(getContext(), "Permission enabled and provider not enabled", Toast.LENGTH_SHORT).show();
                });
            }
        }
    }


    @Override
    public void showLoading() {
        map.getMapAsync(mapboxMap -> {
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
            // add sights
            markersToSights.clear();
            for (int i = 0; i < excursion.getSightSize(); ++i) {
                Sight sight = excursion.getSightAt(i);
                Marker marker = mapboxMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(sight.getLatitude(), sight.getLongitude()))
                                                            .title(sight.getName()));
                markersToSights.put(marker, sight);
            }

            // add paths
            for (int i = 0; i < excursion.getPathSize(); ++i) {
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

            mapboxMap.setOnMarkerClickListener(marker -> {
                if (marker != selectedMarker && markersToSights.containsKey(marker)) {
                    Marker previous = selectedMarker;
                    selectedMarker = marker;

                    if (previous != null) {
                        previous.setIcon(IconFactory.getInstance(getContext()).defaultMarker());
                        mapboxMap.updateMarker(previous);
                    }
                    selectedMarker.setIcon(IconHelper.getIcon(getContext(), CHECKED_SIGHT));
                    mapboxMap.updateMarker(selectedMarker);

                    presenter.onSightClicked(markersToSights.get(selectedMarker), selectedMarker);
                    return true;
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
        Toast.makeText(getContext(), "Network unavailable", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerException() {
        hideLoading();
        Toast.makeText(getContext(), "Api exception", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSightSelection(@NonNull Sight sight, @NonNull Marker marker) {
        map.getMapAsync(mapboxMap -> {
            ExcursionHolderActivity activity = (ExcursionHolderActivity) getActivity();
            if (activity != null) {
                activity.onSightSelected(sight);
            }
        });
    }

    public void removeSightSelection() {
        map.getMapAsync(mapboxMap -> {
            Marker previous = selectedMarker;
            if (previous != null) {
                selectedMarker = null;
                previous.setIcon(IconFactory.getInstance(getContext()).defaultMarker());
                mapboxMap.updateMarker(previous);
            }
        });
    }

}
