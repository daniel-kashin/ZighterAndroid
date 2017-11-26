package com.zighter.zighterandroid.presentation.excursion.holder;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.widget.FrameLayout;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
import com.zighter.zighterandroid.presentation.excursion.sight.SightFragment;

import butterknife.BindView;

public class ExcursionHolderActivity extends BaseSupportActivity {

    @BindView(R.id.bottom_sheet)
    FrameLayout bottomSheet;
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior;

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

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        if (getSupportFragmentManager().findFragmentById(R.id.bottom_sheet) == null) {
            hideBottomSheet();
        } else {
            showBottomSheet();
        }
    }

    public void onSightSelected(@NonNull Sight sight) {
        showBottomSheet();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_sheet, SightFragment.newInstance(sight))
                .commit();
    }

    private void showBottomSheet() {
        bottomSheet.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideBottomSheet() {
        bottomSheet.setVisibility(View.GONE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}
