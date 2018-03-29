package com.zighter.zighterandroid.presentation.excursion.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.presentation.Excursion;
import com.zighter.zighterandroid.data.entities.service.ServicePoint;
import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.data.location.LocationListenerHolder;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.excursion.LocationPermissionListener;
import com.zighter.zighterandroid.presentation.excursion.holder.ExcursionHolderActivity;
import com.zighter.zighterandroid.presentation.login.LoginActivity;
import com.zighter.zighterandroid.util.media.IconHelper;
import com.zighter.zighterandroid.util.media.IconHelperType;
import com.zighter.zighterandroid.util.LocationSettingsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

@SuppressWarnings("CodeBlock2Expr")
public class ExcursionMapFragment extends BaseSupportFragment implements ExcursionMapView,
        LocationListenerHolder.OnLocationChangeListener, LocationPermissionListener {
    private static final String TAG = "ExcursionMapFragment";
    private static final String KEY_EXCURSION_UUID = "KEY_EXCURSION_UUID";

    @NonNull
    public static ExcursionMapFragment newInstance(@NonNull String excursionUuid) {
        ExcursionMapFragment excursionMapFragment = new ExcursionMapFragment();

        Bundle arguments = new Bundle();
        arguments.putString(KEY_EXCURSION_UUID, excursionUuid);
        excursionMapFragment.setArguments(arguments);

        return excursionMapFragment;
    }

    @InjectPresenter
    ExcursionMapPresenter presenter;

    @ProvidePresenter
    public ExcursionMapPresenter providePresenter() {
        if (excursionUuid == null) {
            throw new IllegalStateException();
        }

        return presenterBuilderProvider.get()
                .excursionUuid(excursionUuid)
                .build();
    }

    @Inject
    Provider<ExcursionMapPresenter.Builder> presenterBuilderProvider;
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
    @BindView(R.id.zoom_in)
    ImageView zoomIn;
    @BindView(R.id.zoom_out)
    ImageView zoomOut;
    @BindView(R.id.try_again)
    TextView tryAgain;
    @BindView(R.id.error_message)
    TextView errorMessage;

    @NonNull
    private HashMap<Marker, Sight> markersToSights = new HashMap<>();
    @Nullable
    private Marker selectedMarker;
    @Nullable
    private Marker currentLocationMarker;
    @Nullable
    private MapboxMap mapboxMapToSaveInstanceState;
    @Nullable
    private CameraPosition cameraPositionFromSavedInstanceState;
    @Nullable
    private String excursionUuid = null;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_excursion_map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments == null || !arguments.containsKey(KEY_EXCURSION_UUID)) {
            throw new IllegalStateException();
        } else {
            excursionUuid = arguments.getString(KEY_EXCURSION_UUID);
            if (excursionUuid == null) {
                throw new IllegalStateException();
            }
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof ExcursionHolderActivity)) {
            throw new IllegalStateException("Parent activity must me ExcursionHolderActivity");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews();
        map.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            cameraPositionFromSavedInstanceState = savedInstanceState.getParcelable(MapboxConstants.STATE_CAMERA_POSITION);
        }
    }

    private void initializeViews() {
        map.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        locationImageView.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        zoomIn.setVisibility(View.INVISIBLE);
        zoomOut.setVisibility(View.INVISIBLE);
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

        if (mapboxMapToSaveInstanceState != null) {
            CameraPosition cameraPosition = mapboxMapToSaveInstanceState.getCameraPosition();
            outState.putParcelable(MapboxConstants.STATE_CAMERA_POSITION, cameraPosition);
            mapboxMapToSaveInstanceState = null;
        }
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
    public void onLocationChanged(@NonNull Location location, boolean active) {
        presenter.onLocationChanged(location, active);
    }

    @Override
    public void onLocationProvidersAvailabilityChanged(boolean networkProviderEnabled, boolean gpsProviderEnabled) {
        presenter.onLocationProvidersAvailabilityChanged(networkProviderEnabled, gpsProviderEnabled);
    }

    @Override
    public void showCurrentLocation(@NonNull Location location, boolean active) {
        Log.d(TAG, "showCurrentLocation");
        map.getMapAsync(mapboxMap -> {
            Log.d(TAG, "mapboxMap in showCurrentLocation");

            if (getContext() == null) return;

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            IconHelperType type = active
                    ? IconHelperType.CURRENT_LOCATION_ACTIVE
                    : IconHelperType.CURRENT_LOCATION_OUTDATED;
            Icon icon = IconHelper.getIcon(getContext(), type);

            if (currentLocationMarker != null) {
                currentLocationMarker.setPosition(latLng);
                currentLocationMarker.setIcon(icon);
                mapboxMap.updateMarker(currentLocationMarker);
            } else {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(icon);
                currentLocationMarker = mapboxMap.addMarker(markerOptions);
            }
        });
    }

    @Override
    public void updateLocationAvailability(boolean isPermissionAvailable, boolean isLocationProviderEnabled) {
        if (getActivity() == null) return;

        if (!isPermissionAvailable) {
            locationImageView.setOnClickListener(view -> {
                LocationSettingsHelper.requestOpenPermissionSettings(getActivity());
            });
        } else {
            if (isLocationProviderEnabled) {
                locationImageView.setOnClickListener(view -> {
                    if (currentLocationMarker != null) {
                        map.getMapAsync(mapboxMap -> {
                            mapboxMap.moveCamera(mapboxMapInner -> new CameraPosition.Builder()
                                    .target(currentLocationMarker.getPosition())
                                    .build());
                        });
                    }
                });
            } else {
                locationImageView.setOnClickListener(view -> {
                    LocationSettingsHelper.requestOpenGpsSettings(getActivity());
                });
            }
        }
    }

    @Override
    public void showNotAuthorizedException() {
        if (getContext() == null) {
            return;
        }

        LoginActivity.start(getContext());
    }

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading");
        map.getMapAsync(mapboxMap -> {
            Log.d(TAG, "mapboxMap in showLoading");

            mapboxMap.setOnMarkerClickListener(null);
            tryAgain.setOnClickListener(null);
            tryAgain.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);

            progressBar.setVisibility(View.VISIBLE);
        });
    }

    private void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showExcursion(@NonNull final Excursion excursion) {
        Log.d(TAG, "showExcursion");
        map.getMapAsync(mapboxMap -> {
            Log.d(TAG, "mapboxMap in showExcursion");

            if (getContext() == null) return;

            mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);

            this.mapboxMapToSaveInstanceState = mapboxMap;

            // add sights
            markersToSights.clear();
            for (int i = 0; i < excursion.getSightSize(); ++i) {
                Sight sight = excursion.getSightAt(i);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(sight.getLatitude(), sight.getLongitude()))
                        .icon(IconHelper.getIcon(getContext(), IconHelperType.SIGHT))
                        .title(sight.getName());
                Marker marker = mapboxMap.addMarker(markerOptions);
                markersToSights.put(marker, sight);
            }

            // add paths
            for (int i = 0; i < excursion.getPathSize(); ++i) {
                ServicePath path = excursion.getPathAt(i);

                ServicePoint first = path.getFirstEndpoint();
                mapboxMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(first.getLatitude(), first.getLongitude()))
                                            .icon(IconHelper.getIcon(getContext(), IconHelperType.ENDPOINT_START)));

                ServicePoint second = path.getSecondEndpoint();
                mapboxMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(second.getLatitude(), second.getLongitude()))
                                            .icon(IconHelper.getIcon(getContext(), IconHelperType.ENDPOINT_END)));

                List<LatLng> points = new ArrayList<>();
                for (int j = 0; j < path.getPointSize(); ++j) {
                    ServicePoint point = path.getPointAt(j);
                    points.add(new LatLng(point.getLatitude(), point.getLongitude()));
                }

                mapboxMap.addPolyline(new PolylineOptions()
                                              .addAll(points)
                                              .color(ContextCompat.getColor(getContext(), R.color.navigationBlue))
                                              .alpha(0.7f)
                                              .width(3f));
            }

            mapboxMap.setMaxZoomPreference(excursion.getMaxMapZoom());
            mapboxMap.setMinZoomPreference(excursion.getMinMapZoom());

            if (cameraPositionFromSavedInstanceState != null) {
                mapboxMap.setCameraPosition(cameraPositionFromSavedInstanceState);
            } else {
                LatLng latLng = null;
                if (excursion.getSightSize() != 0) {
                    latLng = new LatLng(excursion.getSightAt(0).getLatitude(),
                                        excursion.getSightAt(0).getLongitude());
                } else if (excursion.getPathSize() != 0) {
                    ServicePath path = excursion.getPathAt(0);
                    if (path.getPointSize() != 0) {
                        latLng = new LatLng(path.getPointAt(0).getLatitude(),
                                            path.getPointAt(0).getLongitude());
                    }
                }

                if (latLng != null) {
                    final LatLng ll = latLng;
                    mapboxMap.moveCamera(mapboxMapInner -> new CameraPosition.Builder()
                            .target(ll)
                            .zoom(13)
                            .build());
                }
            }

            mapboxMap.setOnMarkerClickListener(marker -> {
                if (marker != selectedMarker && markersToSights.containsKey(marker)) {
                    ExcursionHolderActivity activity = (ExcursionHolderActivity) getActivity();
                    if (activity != null && activity.onSightSelected(markersToSights.get(marker))) {
                        presenter.onSightClicked(markersToSights.get(marker));
                    }
                }
                return true;
            });

            zoomIn.setOnClickListener(view -> {
                mapboxMap.animateCamera(it -> {
                    CameraPosition current = it.getCameraPosition();
                    return new CameraPosition.Builder()
                            .target(current.target)
                            .zoom(current.zoom + 1)
                            .tilt(current.tilt)
                            .bearing(current.bearing)
                            .build();
                });
            });

            zoomOut.setOnClickListener(view -> {
                mapboxMap.animateCamera(it -> {
                    CameraPosition current = it.getCameraPosition();
                    return new CameraPosition.Builder()
                            .target(current.target)
                            .zoom(current.zoom - 1)
                            .tilt(current.tilt)
                            .bearing(current.bearing)
                            .build();
                });
            });


            hideLoading();
            tryAgain.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
            map.setVisibility(View.VISIBLE);
            locationImageView.setVisibility(View.VISIBLE);
            zoomOut.setVisibility(View.VISIBLE);
            zoomIn.setVisibility(View.VISIBLE);

            logAllSightMarkers(mapboxMap);
        });
    }

    @Override
    public void showNetworkUnavailable() {
        Log.d(TAG, "showNetworkUnavailable");
        if (getContext() == null) {
            return;
        }

        hideLoading();

        errorMessage.setText(getContext().getString(R.string.network_error_message));
        tryAgain.setOnClickListener(view -> presenter.onReloadExcursionRequest());
        errorMessage.setVisibility(View.VISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public void showUnhandledException() {
        if (getContext() == null) {
            return;
        }

        hideLoading();

        errorMessage.setText(getContext().getString(R.string.unhandled_error_message));
        tryAgain.setOnClickListener(view -> presenter.onReloadExcursionRequest());
        errorMessage.setVisibility(View.VISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerException() {
        Log.d(TAG, "showServerException");
        if (getContext() == null) {
            return;
        }

        hideLoading();

        errorMessage.setText(getContext().getString(R.string.server_error_message));
        tryAgain.setOnClickListener(view -> presenter.onReloadExcursionRequest());
        errorMessage.setVisibility(View.VISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSightSelection(@Nullable Sight sight) {
        Log.d(TAG, "showSightSelection(" +
                (sight != null ? sight.getLongitude() : "null") +
                ", " +
                (sight != null ? sight.getLatitude() : "null") +
                ")");

        map.getMapAsync(mapboxMap -> {
            Log.d(TAG, "mapboxMap in showSightSelection");

            if (getContext() == null) return;

            Marker previous = selectedMarker;
            if (previous != null) {
                previous.setIcon(IconHelper.getIcon(getContext(), IconHelperType.SIGHT));
                mapboxMap.updateMarker(previous);
            }

            if (sight == null) {
                selectedMarker = null;
                return;
            }

            Marker marker = null;
            Set<Map.Entry<Marker, Sight>> set = markersToSights.entrySet();
            for (Map.Entry<Marker, Sight> entry : set) {
                if (sight.equals(entry.getValue())) {
                    marker = entry.getKey();
                }
            }

            if (marker == null) {
                selectedMarker = null;
                return;
            }

            selectedMarker = marker;
            selectedMarker.setIcon(IconHelper.getIcon(getContext(), IconHelperType.CHECKED_SIGHT));
            mapboxMap.updateMarker(selectedMarker);

            logAllSightMarkers(mapboxMap);
        });
    }

    public void removeSightSelection() {
        presenter.onSightClicked(null);
    }

    private void logAllSightMarkers(@NonNull MapboxMap mapboxMap) {
        List<Marker> markers = mapboxMap.getMarkers();
        if (markers.isEmpty()) {
            Log.d(TAG, "markers are empty");
        } else {
            StringBuilder stringBuilder = new StringBuilder("  markers [");
            for (Marker marker : markers) {
                if (markersToSights.containsKey(marker)) {
                    LatLng position = marker.getPosition();
                    boolean equalsSelected = marker.equals(selectedMarker);

                    stringBuilder.append("(");
                    if (equalsSelected) {
                        stringBuilder.append("!");
                    }
                    stringBuilder.append(position.getLongitude());
                    stringBuilder.append(", ");
                    stringBuilder.append(position.getLatitude());
                    if (equalsSelected) {
                        stringBuilder.append("!");
                    }
                    stringBuilder.append(") ");
                }
            }
            stringBuilder.append("]");

            Log.d(TAG, stringBuilder.toString());
        }
    }
}
