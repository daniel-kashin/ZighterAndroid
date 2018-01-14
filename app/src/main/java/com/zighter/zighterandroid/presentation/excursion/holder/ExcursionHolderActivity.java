package com.zighter.zighterandroid.presentation.excursion.holder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
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
import static com.zighter.zighterandroid.util.LocationSettingsHelper.LOCATION_PERMISSION;

public class ExcursionHolderActivity extends BaseSupportActivity {
    private boolean bottomSheetNeedsRedrawing = false;
    private CustomBottomSheetBehavior<NestedScrollView> bottomSheetBehavior;

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

        if (!LocationSettingsHelper.isLocationPermissionEnabled(this)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, LOCATION_PERMISSION)) {
                LocationSettingsHelper.showLocationPermissionRationale(this);
            } else {
                LocationSettingsHelper.requestLocationPermission(this);
            }
        } else {
            LocationSettingsHelper.ensureGpsEnabled(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationSettingsHelper.LOCATION_PERMISSION_REQUEST_CODE:
                if (LocationSettingsHelper.isLocationPermissionEnabled(this)) {
                    LocationSettingsHelper.ensureGpsEnabled(this);
                }
                break;
            case LocationSettingsHelper.GPS_REQUEST_CODE:
                // TODO
                break;
        }
    }

    public void onSightSelected(@NonNull Sight sight) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_content, SightFragment.newInstance(sight))
                .commit();
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
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        LocationSettingsHelper.ensureGpsEnabled(this);
                    } else {
                        LocationSettingsHelper.showLocationPermissionRationale(this);
                    }
                }
            }
        }
    }

    @Nullable
    private SightFragment getSightFragment() {
        Fragment sightFragment = getSupportFragmentManager().findFragmentById(R.id.bottom_sheet_content);
        if (sightFragment != null) {
            if (!(sightFragment instanceof SightFragment)) {
                throw new IllegalStateException("Fragment containing in bottom sheet must be instance of SightFragment");
            } else {
                return (SightFragment) sightFragment;
            }
        }
        return null;
    }
}
