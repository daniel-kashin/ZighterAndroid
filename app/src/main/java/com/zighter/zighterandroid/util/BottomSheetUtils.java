package com.zighter.zighterandroid.util;

import android.support.v4.view.ViewPager;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class BottomSheetUtils {

    public static void setupViewPager(ViewPager pager) {
        final View bottomSheetView = findBottomSheetView(pager);
        if (bottomSheetView != null) {
            pager.addOnPageChangeListener(new BottomSheetViewPagerListener(pager, bottomSheetView));
        }
    }

    private static class BottomSheetViewPagerListener extends ViewPager.SimpleOnPageChangeListener {
        private final View pager;
        private final AnchorBottomSheetBehavior behavior;

        private BottomSheetViewPagerListener(ViewPager pager, View bottomSheetView) {
            this.pager = pager;
            this.behavior = AnchorBottomSheetBehavior.from(bottomSheetView);
        }

        @Override
        public void onPageSelected(int position) {
            pager.post(new Runnable() {
                @Override
                public void run() {
                    behavior.invalidateScrollingChild();
                }
            });
        }
    }

    private static View findBottomSheetView(View root) {
        View current = root;
        while (current != null) {
            final ViewGroup.LayoutParams params = current.getLayoutParams();
            if (params instanceof CoordinatorLayout.LayoutParams
                    && ((CoordinatorLayout.LayoutParams) params).getBehavior() instanceof AnchorBottomSheetBehavior) {
                return current;
            }
            final ViewParent parent = current.getParent();
            current = parent == null || !(parent instanceof View) ? null : (View) parent;
        }
        return null;
    }

}