package com.zighter.zighterandroid.presentation.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.pushtorefresh.storio3.sqlite.operations.internal.RxJavaUtils;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.presentation.bought_excursions.BoughtExcursionsAdapter;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

public class SearchFragment extends BaseSupportFragment implements SearchView {
    @NonNull
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @InjectPresenter
    SearchPresenter presenter;

    @ProvidePresenter
    public SearchPresenter providePresenter() {
        return searchPresenter;
    }

    @Inject
    SearchPresenter searchPresenter;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getSearchComponent()
                .inject(this);
    }

    @BindView(R.id.toolbar_search)
    EditText toolbarSearch;
    @BindView(R.id.icon_clear)
    ImageView iconClear;
    @BindView(R.id.try_again)
    TextView tryAgain;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.error_message)
    TextView errorMessage;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
    }

    private void initializeView() {
        tryAgain.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        RxTextView.textChanges(toolbarSearch)
                .map(editable -> editable.toString().trim())
                .flatMap(string -> {
                    if (string.isEmpty()) {
                        return Observable.just(string);
                    } else {
                        return Observable.just(string)
                                .debounce(500, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(string -> presenter.onSearchTyped(string));

        RxView.clicks(iconClear)
                .subscribe(it -> toolbarSearch.setText(null));
    }

    @Override
    public void showEmptySearch() {
        errorMessage.setText(getString(R.string.type_search_excursions));

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showEmptyExcursions() {
        if (getContext() == null) {
            return;
        }

        errorMessage.setText(getContext().getString(R.string.nothing_was_found));
        tryAgain.setOnClickListener(view -> presenter.onSearchTyped(toolbarSearch.getText().toString()));

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showExcursions(@NonNull List<BoughtExcursionWithStatus> excursions) {
        // TODO

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showUnhandledException() {
        if (getContext() == null) {
            return;
        }

        errorMessage.setText(getContext().getString(R.string.unhandled_error_message));
        tryAgain.setOnClickListener(view -> presenter.onSearchTyped(toolbarSearch.getText().toString()));

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNetworkUnavailable() {
        if (getContext() == null) {
            return;
        }

        errorMessage.setText(getContext().getString(R.string.network_error_message));
        tryAgain.setOnClickListener(view -> presenter.onSearchTyped(toolbarSearch.getText().toString()));

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
        tryAgain.setOnClickListener(view -> presenter.onSearchTyped(toolbarSearch.getText().toString()));

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
}
