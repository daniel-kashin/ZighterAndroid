package com.zighter.zighterandroid.presentation.my_excursions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.excursion.holder.ExcursionHolderActivity;

import butterknife.BindView;

public class MyExcursionsFragment extends BaseSupportFragment {

    public static MyExcursionsFragment newInstance() {
        return new MyExcursionsFragment();
    }

    @BindView(R.id.root_view)
    ConstraintLayout rootView;

    @Override
    protected void onInjectDependencies() {
        // do nothing
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_excursions;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ExcursionHolderActivity.class);
            getContext().startActivity(intent);
        });
    }
}
