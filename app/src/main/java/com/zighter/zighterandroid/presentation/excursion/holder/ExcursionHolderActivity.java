package com.zighter.zighterandroid.presentation.excursion.holder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
import com.zighter.zighterandroid.presentation.excursion.map.ExcursionMapFragment;
import com.zighter.zighterandroid.presentation.excursion.sight.SightFragment;
import com.zighter.zighterandroid.util.CustomBottomSheetBehavior;
import com.zighter.zighterandroid.util.LocationSettingsHelper;

import butterknife.BindView;

import static com.zighter.zighterandroid.util.CustomBottomSheetBehavior.STATE_COLLAPSED;
import static com.zighter.zighterandroid.util.CustomBottomSheetBehavior.STATE_HIDDEN;

public class ExcursionHolderActivity extends BaseSupportActivity {
    private boolean bottomSheetNeedsRedrawing = false;
    private CustomBottomSheetBehavior<NestedScrollView> bottomSheetBehavior;
    private boolean locationPermissionNeedsRefresh = false;

    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;

    @Override
    protected void onInjectDependencies() {
        // do nothing
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_excursion_holder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeBottomSheet();
        if (getSupportFragmentManager().findFragmentById(R.id.bottom_sheet_content) == null) {
            hideBottomSheet();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationPermissionNeedsRefresh = false;
        if (LocationSettingsHelper.isLocationPermissionGranted(this)) {
            onRequestLocationPermissionResult(true);
        } else {
            LocationSettingsHelper.requestLocationPermission(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationPermissionNeedsRefresh) {
            onRequestLocationPermissionResult(LocationSettingsHelper.isLocationPermissionGranted(this));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationPermissionNeedsRefresh = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationSettingsHelper.LOCATION_PERMISSION_REQUEST_CODE:
                boolean isLocationPermissionEnabled = LocationSettingsHelper.isLocationPermissionGranted(this);
                onRequestLocationPermissionResult(isLocationPermissionEnabled);
                break;
        }
    }

    public void onSightSelected(@NonNull Sight sight) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_content, SightFragment.newInstance(sight))
                .commitNow();
        onRequestLocationPermissionResultForSight(LocationSettingsHelper.isLocationPermissionGranted(this));
    }

    public void onSightShown() {
        showBottomSheet();
    }


    private void showBottomSheet() {
        if (bottomSheet.getVisibility() != View.VISIBLE) {
            bottomSheetNeedsRedrawing = true;
            bottomSheetBehavior.setState(STATE_COLLAPSED);
        }
    }

    private void hideBottomSheet() {
        bottomSheet.setVisibility(View.INVISIBLE);
        bottomSheetBehavior.setState(STATE_HIDDEN);
    }

    private void initializeBottomSheet() {
        bottomSheetBehavior = CustomBottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case STATE_HIDDEN:
                        bottomSheet.setVisibility(View.INVISIBLE);

                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.excursion_map_fragment);
                        if (fragment == null || !(fragment instanceof ExcursionMapFragment)) {
                            throw new IllegalStateException("Excursion map fragment must be an instance of ExcursionMapFragment");
                        }
                        ((ExcursionMapFragment) fragment).removeSightSelection();

                        SightFragment sightFragment = getSightFragment();
                        if (sightFragment != null) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .remove(getSightFragment())
                                    .commit();
                        }
                        break;
                    case STATE_COLLAPSED:
                        if (bottomSheetNeedsRedrawing) {
                            bottomSheetNeedsRedrawing = false;
                            bottomSheet.requestLayout();
                            bottomSheet.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LocationSettingsHelper.LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    onRequestLocationPermissionResult(permissionGranted);
                }
            }
        }
    }

    private void onRequestLocationPermissionResult(boolean permissionGranted) {
        ExcursionMapFragment excursionMapFragment = getExcursionMapFragment();
        SightFragment sightFragment = getSightFragment();
        if (excursionMapFragment != null) {
            excursionMapFragment.onLocationPermissionGranted(permissionGranted);
        }
        if (sightFragment != null) {
            sightFragment.onLocationPermissionGranted(permissionGranted);
        }
    }

    private void onRequestLocationPermissionResultForSight(boolean permissionGranted) {
        SightFragment sightFragment = getSightFragment();
        if (sightFragment != null) {
            sightFragment.onLocationPermissionGranted(permissionGranted);
        }
    }

    @Nullable
    private SightFragment getSightFragment() {
        Fragment sightFragment = getSupportFragmentManager().findFragmentById(R.id.bottom_sheet_content);
        if (sightFragment != null) {
            if (!(sightFragment instanceof SightFragment)) {
                throw new IllegalStateException("Fragment containing in bottom sheet must be an instance of SightFragment");
            } else {
                return (SightFragment) sightFragment;
            }
        }
        return null;
    }

    @Nullable
    private ExcursionMapFragment getExcursionMapFragment() {
        Fragment excursionMapFragment = getSupportFragmentManager().findFragmentById(R.id.excursion_map_fragment);
        if (excursionMapFragment != null) {
            if (!(excursionMapFragment instanceof ExcursionMapFragment)) {
                throw new IllegalStateException("Excursion map fragment must be an instance of ExcursionMapFragment");
            } else {
                return (ExcursionMapFragment) excursionMapFragment;
            }
        }
        return null;
    }
}
