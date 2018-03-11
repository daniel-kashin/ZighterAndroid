package com.zighter.zighterandroid.data.mapbox;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;
import com.zighter.zighterandroid.data.entities.presentation.Excursion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

public class MapboxHelper {
    private static final String KEY_UUID = "KEY_UUID";

    private static boolean metadataEquals(@NonNull byte[] first, @NonNull byte[] second) {
        if (first.length != second.length) {
            return false;
        }

        for (int i = 0; i < first.length; ++i) {
            if (first[i] != second[i]) {
                return false;
            }
        }

        return true;
    }

    @NonNull
    private static OfflineTilePyramidRegionDefinition getDefinition(@NonNull Context context,
                                                                    @NonNull Excursion excursion) {
        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(excursion.getWestNorthMapBound().getLatitude(),
                                    excursion.getEastSouthMapBound().getLongitude())) // Northeast
                .include(new LatLng(excursion.getEastSouthMapBound().getLatitude(),
                                    excursion.getWestNorthMapBound().getLongitude())) // Southwest
                .build();

        return new OfflineTilePyramidRegionDefinition(Style.MAPBOX_STREETS,
                                                      latLngBounds,
                                                      excursion.getMinMapZoom(),
                                                      excursion.getMinMapZoom(),
                                                      context.getResources().getDisplayMetrics().density);
    }

    @Nullable
    private static byte[] getMetadata(@NonNull Excursion excursion) {
        byte[] metadata;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(KEY_UUID, excursion.getUuid());
            String json = jsonObject.toString();
            metadata = json.getBytes();
        } catch (Exception exception) {
            metadata = null;
        }
        return metadata;
    }

    public static void createOfflineRegion(@NonNull Context context,
                                           @NonNull Excursion excursion,
                                           @NonNull MapboxRegionCreateListener listener) {
        OfflineTilePyramidRegionDefinition definition = MapboxHelper.getDefinition(context, excursion);
        byte[] metadata = MapboxHelper.getMetadata(excursion);
        if (metadata == null) {
            listener.onError();
            return;
        }
        OfflineManager offlineManager = OfflineManager.getInstance(context);
        handleCreateOfflineRegion(offlineManager, definition, metadata, listener);
    }

    private static void handleCreateOfflineRegion(@NonNull OfflineManager offlineManager,
                                                  @NonNull OfflineTilePyramidRegionDefinition definition,
                                                  @NonNull byte[] metadata,
                                                  @NonNull MapboxRegionCreateListener listener) {
        if (listener.isDisposed()) {
            return;
        }

        offlineManager.createOfflineRegion(definition, metadata, new OfflineManager.CreateOfflineRegionCallback() {
            private int previousPercentage = -1;

            @Override
            public void onCreate(OfflineRegion offlineRegion) {
                offlineRegion.setDeliverInactiveMessages(false);
                offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                    @Override
                    public void onStatusChanged(OfflineRegionStatus status) {
                        if (!listener.isDisposed()) {
                            if (status.isComplete()) {
                                listener.onComplete();
                            } else {
                                int percentage = status.getRequiredResourceCount() >= 0
                                        ? (int) (100.0 * status.getCompletedResourceCount() / status.getRequiredResourceCount()) :
                                        0;

                                if (percentage >= 100) {
                                    percentage = 99;
                                } else if (percentage < 0) {
                                    percentage = 0;
                                }

                                if (percentage != previousPercentage) {
                                    previousPercentage = percentage;
                                    listener.onProgressChanged(percentage, 100);
                                }
                            }
                        } else {
                            offlineRegion.setObserver(null);
                        }
                    }

                    @Override
                    public void onError(OfflineRegionError error) {
                        offlineRegion.setObserver(null);
                        offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
                        listener.onError();
                    }

                    @Override
                    public void mapboxTileCountLimitExceeded(long limit) {
                        offlineRegion.setObserver(null);
                        offlineRegion.setDownloadState(OfflineRegion.STATE_INACTIVE);
                        listener.onError();
                    }
                });
                offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
            }

            @Override
            public void onError(String error) {
                listener.onError();
            }
        });
    }

    public interface MapboxRegionCreateListener {
        boolean isDisposed();

        void onProgressChanged(int currentPosition, int requiredCount);

        void onComplete();

        void onError();
    }

    private static class EmptyOfflineRegionDeleteCallback implements OfflineRegion.OfflineRegionDeleteCallback {
        @Override
        public void onDelete() {
            // do nothing
        }

        @Override
        public void onError(String error) {
            // do nothing
        }
    }
}
