package com.zighter.zighterandroid.presentation.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.SupportMapFragment;
import com.zighter.zighterandroid.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    @BindView(R.id.map_view)
    MapView map;


    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        map.onCreate(savedInstanceState);
        map.getMapAsync(mapboxMap -> {
            int i = 0;
        });
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
        super.onPause();
        map.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        map.onDestroy();
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
    public void onMapReady(MapboxMap mapboxMap) {
        Toast.makeText(getContext(), "OnMapReady", Toast.LENGTH_SHORT).show();
    }
}
