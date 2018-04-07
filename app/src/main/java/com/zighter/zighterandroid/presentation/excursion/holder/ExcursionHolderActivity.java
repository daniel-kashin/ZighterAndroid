package com.zighter.zighterandroid.presentation.excursion.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursion;
import com.zighter.zighterandroid.data.entities.presentation.Sight;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
import com.zighter.zighterandroid.presentation.excursion.map.ExcursionMapFragment;
import com.zighter.zighterandroid.presentation.excursion.sight.SightFragment;
import com.zighter.zighterandroid.presentation.guide.GuideActivity;
import com.zighter.zighterandroid.view.AnchorBottomSheetBehavior;
import com.zighter.zighterandroid.util.LocationSettingsHelper;

import java.io.Serializable;

import butterknife.BindView;

import static com.zighter.zighterandroid.view.AnchorBottomSheetBehavior.STATE_COLLAPSED;
import static com.zighter.zighterandroid.view.AnchorBottomSheetBehavior.STATE_DRAGGING;
import static com.zighter.zighterandroid.view.AnchorBottomSheetBehavior.STATE_HIDDEN;
import static com.zighter.zighterandroid.view.AnchorBottomSheetBehavior.STATE_SETTLING;

public class ExcursionHolderActivity extends BaseSupportActivity {
    private static final String TAG = "ExcursionHolderActivity";
    private static final String KEY_BOTTOM_SHEET_VISIBLE = "KEY_BOTTOM_SHEET_VISIBLE";
    private static final String KEY_BOUGHT_EXCURSION = "KEY_BOUGHT_EXCURSION";

    public static void start(@NonNull Context context, @NonNull BoughtExcursion boughtExcursion) {
        Intent intent = new Intent(context, ExcursionHolderActivity.class);
        intent.putExtra(KEY_BOUGHT_EXCURSION, (Serializable) boughtExcursion);
        context.startActivity(intent);
    }

    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;
    @BindView(R.id.root_view)
    CoordinatorLayout rootView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.icon_guide)
    ImageView iconGuide;

    private AnchorBottomSheetBehavior<NestedScrollView> bottomSheetBehavior;
    private BoughtExcursion boughtExcursion;
    private boolean locationPermissionNeedsRefresh = false;
    private boolean showBottomSheetAfterHide = false;

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

        initializeBoughtExcursion();
        if (getSupportFragmentManager().findFragmentById(R.id.excursion_map_fragment_holder) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.excursion_map_fragment_holder, ExcursionMapFragment.newInstance(boughtExcursion.getUuid()))
                    .commitNow();
        }

        initializeToolbar();
        initializeBottomSheet();
        if (getSupportFragmentManager().findFragmentById(R.id.bottom_sheet_fragment_container) == null) {
            hideBottomSheet();
        } else {
            showBottomSheet();
        }
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarTitle.setText(boughtExcursion.getName());

        if (boughtExcursion.isGuideAvailable()) {
            iconGuide.setVisibility(View.VISIBLE);
            iconGuide.setOnClickListener(view -> {
                GuideActivity.start(ExcursionHolderActivity.this, boughtExcursion.getOwnerUuid());
            });
        } else {
            iconGuide.setVisibility(View.GONE);
        }
    }

    private void initializeBoughtExcursion() {
        if (getIntent() == null) {
            throw new IllegalStateException();
        }

        BoughtExcursion boughtExcursion = (BoughtExcursion) getIntent().getSerializableExtra(KEY_BOUGHT_EXCURSION);
        if (boughtExcursion == null) {
            throw new IllegalStateException();
        }

        this.boughtExcursion = boughtExcursion;
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationPermissionNeedsRefresh = false;
        if (LocationSettingsHelper.isLocationPermissionGranted(this)) {
            onRequestLocationPermissionResult(true, true, true);
        } else {
            LocationSettingsHelper.requestLocationPermission(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationPermissionNeedsRefresh) {
            boolean isLocationPermissionGranted = LocationSettingsHelper.isLocationPermissionGranted(this);
            onRequestLocationPermissionResult(isLocationPermissionGranted, true, true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationPermissionNeedsRefresh = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_BOTTOM_SHEET_VISIBLE, bottomSheet.getVisibility() == View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LocationSettingsHelper.LOCATION_PERMISSION_REQUEST_CODE:
                boolean isLocationPermissionGranted = LocationSettingsHelper.isLocationPermissionGranted(this);
                onRequestLocationPermissionResult(isLocationPermissionGranted, true, true);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public boolean onSightSelected(@NonNull Sight sight) {
        Log.d(TAG, "onSightSelected(" + bottomSheetBehavior.getState() + ")");

        if (!isBottomSheetSliding(bottomSheetBehavior.getState())) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.bottom_sheet_fragment_container, SightFragment.newInstance(sight))
                    .commitNow();

            showBottomSheet();

            boolean locationPermissionGranted = LocationSettingsHelper.isLocationPermissionGranted(this);
            onRequestLocationPermissionResult(locationPermissionGranted, true, false);

            return true;
        } else {
            return false;
        }
    }


    private void showBottomSheet() {
        Log.d(TAG, "showBottomSheet(" + bottomSheetBehavior.getState() + ")");
        bottomSheet.setVisibility(View.VISIBLE);
        if (!isBottomSheetSliding(bottomSheetBehavior.getState())) {
            int state = bottomSheetBehavior.getState();
            if (state == STATE_HIDDEN) {
                bottomSheetBehavior.setState(STATE_COLLAPSED);
            } else {
                showBottomSheetAfterHide = true;
                bottomSheetBehavior.setState(STATE_HIDDEN);
            }
        }
    }

    private void hideBottomSheet() {
        Log.d(TAG, "hideBottomSheet");
        showBottomSheetAfterHide = false;
        bottomSheetBehavior.setState(STATE_HIDDEN);
    }

    private void initializeBottomSheet() {
        bottomSheet.setVisibility(View.INVISIBLE);
        bottomSheetBehavior = AnchorBottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.addBottomSheetCallback(new AnchorBottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("WrongConstant")
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.d(TAG, "onStateChanged(" + newState + ", " + showBottomSheetAfterHide + ")");
                switch (newState) {
                    case STATE_HIDDEN: {
                        if (showBottomSheetAfterHide) {
                            showBottomSheetAfterHide = false;
                            bottomSheet.setVisibility(View.VISIBLE);
                            bottomSheetBehavior.setState(STATE_COLLAPSED);
                        } else {
                            bottomSheet.setVisibility(View.INVISIBLE);

                            ExcursionMapFragment fragment = getExcursionMapFragment();
                            if (fragment != null) {
                                fragment.removeSightSelection();
                            }

                            SightFragment sightFragment = getSightFragment();
                            if (sightFragment != null) {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .remove(getSightFragment())
                                        .commit();
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d(TAG, "onSlide(" + slideOffset + ")");
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
                    onRequestLocationPermissionResult(permissionGranted, true, true);
                }
            }
        }
    }

    private void onRequestLocationPermissionResult(boolean permissionGranted, boolean notifySight, boolean notifyExcursionMap) {
        if (notifySight) {
            SightFragment sightFragment = getSightFragment();
            if (sightFragment != null) {
                sightFragment.onLocationPermissionGranted(permissionGranted);
            }
        }
        if (notifyExcursionMap) {
            ExcursionMapFragment excursionMapFragment = getExcursionMapFragment();
            if (excursionMapFragment != null) {
                excursionMapFragment.onLocationPermissionGranted(permissionGranted);
            }
        }
    }

    @Nullable
    private SightFragment getSightFragment() {
        Fragment sightFragment = getSupportFragmentManager().findFragmentById(R.id.bottom_sheet_fragment_container);
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
        Fragment excursionMapFragment = getSupportFragmentManager().findFragmentById(R.id.excursion_map_fragment_holder);
        if (excursionMapFragment != null) {
            if (!(excursionMapFragment instanceof ExcursionMapFragment)) {
                throw new IllegalStateException("Excursion map fragment must be an instance of ExcursionMapFragment");
            } else {
                return (ExcursionMapFragment) excursionMapFragment;
            }
        }
        return null;
    }

    private boolean isBottomSheetSliding(int bottomSheetState) {
        return bottomSheetState == STATE_SETTLING || bottomSheetState == STATE_DRAGGING;
    }
}
