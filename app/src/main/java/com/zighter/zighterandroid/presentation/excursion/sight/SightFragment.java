package com.zighter.zighterandroid.presentation.excursion.sight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.data.entities.service.Sight;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;

import butterknife.BindView;

public class SightFragment extends BaseSupportFragment {

    private static final String KEY_SIGHT = "SIGHT";

    public static SightFragment newInstance(@NonNull Sight sight) {
        SightFragment sightFragment = new SightFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(KEY_SIGHT, sight);
        sightFragment.setArguments(arguments);

        return sightFragment;
    }

    @BindView(R.id.sight_name)
    TextView sightName;

    private Sight sight;

    @Override
    protected void onInjectDependencies() {
        // do nothing
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sight;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (!getArguments().containsKey(KEY_SIGHT) || getArguments().getSerializable(KEY_SIGHT) == null) {
            throw new IllegalStateException("SightFragment must be created using newInstance method");
        }

        sight = (Sight) getArguments().getSerializable(KEY_SIGHT);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO
        sightName.setText("С сервера не пришло имя");
    }
}
