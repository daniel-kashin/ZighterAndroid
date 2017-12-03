package com.zighter.zighterandroid.presentation.excursion.holder;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
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

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1223;

    @BindView(R.id.bottom_sheet)
    NestedScrollView bottomSheet;

    private CustomBottomSheetBehavior<NestedScrollView> bottomSheetBehavior;

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
        } else {
            showBottomSheet();
        }
    }

    public void onSightSelected(@NonNull Sight sight) {
        showBottomSheet();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet_content, SightFragment.newInstance(sight))
                .commit();
    }

    private void showBottomSheet() {
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(STATE_COLLAPSED);
    }

    private void hideBottomSheet() {
        bottomSheet.setVisibility(View.GONE);
        bottomSheetBehavior.setState(STATE_HIDDEN);
    }

    private void initializeBottomSheet() {
        bottomSheetBehavior = CustomBottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case STATE_DRAGGING:
                        //Toast.makeText(ExcursionHolderActivity.this, "DRAGGING", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_COLLAPSED:
                        //Toast.makeText(ExcursionHolderActivity.this, "COLLAPSED", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_EXPANDED:
                        //Toast.makeText(ExcursionHolderActivity.this, "EXPANDED", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_HIDDEN:
                        bottomSheetBehavior.setState(STATE_COLLAPSED);
                        //Toast.makeText(ExcursionHolderActivity.this, "HIDDEN", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_ANCHOR_POINT:
                        //Toast.makeText(ExcursionHolderActivity.this, "ANCHOR", Toast.LENGTH_SHORT).show();
                        break;
                    case STATE_SETTLING:
                        //Toast.makeText(ExcursionHolderActivity.this, "SETTLING", Toast.LENGTH_SHORT).show();
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
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Fragment sightFragment = getSupportFragmentManager().findFragmentById(R.id.bottom_sheet_content);
                    if (sightFragment != null) {
                        ((SightFragment) sightFragment).onLocationPermissionEnabled();
                    }
                }
            }
        }
    }

    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{SightPresenter.LOCATION_PERMISSION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

}
