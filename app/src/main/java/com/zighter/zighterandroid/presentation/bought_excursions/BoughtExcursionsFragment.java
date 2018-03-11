package com.zighter.zighterandroid.presentation.bought_excursions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.excursion.holder.ExcursionHolderActivity;
import com.zighter.zighterandroid.util.recyclerview.SimpleDividerItemDecoration;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

import static android.support.v7.widget.RecyclerView.NO_POSITION;
import static com.zighter.zighterandroid.presentation.bought_excursions.BoughtExcursionsAdapter.OnBoughtExcursionClickListener;
import static com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus.DownloadStatus;

public class BoughtExcursionsFragment extends BaseSupportFragment
        implements BoughtExcursionsView, OnBoughtExcursionClickListener {

    public static BoughtExcursionsFragment newInstance() {
        return new BoughtExcursionsFragment();
    }

    @InjectPresenter
    BoughtExcursionsPresenter presenter;

    @ProvidePresenter
    public BoughtExcursionsPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Inject
    Provider<BoughtExcursionsPresenter> presenterProvider;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getBoughtExcursionsMediaComponent()
                .inject(this);
    }

    @BindView(R.id.root_view)
    ConstraintLayout rootView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.try_again)
    TextView tryAgain;
    @BindView(R.id.error_message)
    TextView errorMessage;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_excursions;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
    }

    private void initializeView() {
        if (getContext() == null) {
            return;
        }

        recyclerView.setAdapter(new BoughtExcursionsAdapter(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                                              LinearLayoutManager.VERTICAL,
                                                              false));

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showExcursions(@NonNull List<BoughtExcursionWithStatus> excursions) {
        BoughtExcursionsAdapter adapter = (BoughtExcursionsAdapter) recyclerView.getAdapter();
        adapter.setExcursions(excursions);
        adapter.notifyDataSetChanged();

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showExcursionStatus(@NonNull String boughtExcursionUuid, @NonNull DownloadStatus downloadStatus) {
        BoughtExcursionsAdapter adapter = (BoughtExcursionsAdapter) recyclerView.getAdapter();
        int position = adapter.refreshExcursionStatus(boughtExcursionUuid, downloadStatus);
        if (position != NO_POSITION) {
            adapter.notifyItemChanged(position);
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNetworkUnavailable() {
        if (getContext() == null) {
            return;
        }

        errorMessage.setText(getContext().getString(R.string.network_error_message));
        tryAgain.setOnClickListener(view -> presenter.onReloadExcursionsRequest());

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showServerException() {
        if (getContext() == null) {
            return;
        }

        errorMessage.setText(getContext().getString(R.string.server_error_message));
        tryAgain.setOnClickListener(view -> presenter.onReloadExcursionsRequest());

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showUnhandledException() {
        if (getContext() == null) {
            return;
        }

        errorMessage.setText(getContext().getString(R.string.unhandled_error_message));
        tryAgain.setOnClickListener(view -> presenter.onReloadExcursionsRequest());

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showEmptyExcursions() {
        if (getContext() == null) {
            return;
        }

        errorMessage.setText(getContext().getString(R.string.empty_bought_excursions));
        tryAgain.setOnClickListener(view -> presenter.onReloadExcursionsRequest());

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBoughtExcursionClicked(@NonNull BoughtExcursionWithStatus boughtExcursionWithStatus) {
        if (getContext() == null) {
            return;
        }

        ExcursionHolderActivity.start(getContext(), boughtExcursionWithStatus.getExcursion().getUuid());
    }

    @Override
    public void onDownloadClicked(@NonNull BoughtExcursionWithStatus boughtExcursionWithStatus) {
        presenter.onDownloadClicked(boughtExcursionWithStatus);
    }
}
