package com.zighter.zighterandroid.presentation.excursion.holder;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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
import com.zighter.zighterandroid.presentation.excursion.sight.SightPresenter;
import com.zighter.zighterandroid.util.CustomBottomSheetBehavior;

import butterknife.BindView;

import static com.zighter.zighterandroid.util.CustomBottomSheetBehavior.STATE_ANCHOR_POINT;
import static com.zighter.zighterandroid.util.CustomBottomSheetBehavior.STATE_COLLAPSED;
import static com.zighter.zighterandroid.util.CustomBottomSheetBehavior.STATE_DRAGGING;
import static com.zighter.zighterandroid.util.CustomBottomSheetBehavior.STATE_EXPANDED;
import static com.zighter.zighterandroid.util.CustomBottomSheetBehavior.STATE_HIDDEN;
import static com.zighter.zighterandroid.util.CustomBottomSheetBehavior.STATE_SETTLING;

public class ExcursionHolderActivity extends BaseSupportActivity {
    private static final int SIGHT_FRAGMENT_LOCATION_PERMISSION_REQUEST_CODE = 1223;

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


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case SIGHT_FRAGMENT_LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SightFragment sightFragment = getSightFragment();
                    if (sightFragment != null) {
                        sightFragment.onLocationPermissionEnabled();
                    }
                }
            }
        }
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                                          new String[]{SightPresenter.LOCATION_PERMISSION},
                                          SIGHT_FRAGMENT_LOCATION_PERMISSION_REQUEST_CODE);
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
