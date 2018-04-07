package com.zighter.zighterandroid.presentation.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchExcursion;
import com.zighter.zighterandroid.data.entities.service.ServiceSearchSort;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.login.LoginActivity;
import com.zighter.zighterandroid.view.recyclerview.SimpleDividerItemDecoration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.zighter.zighterandroid.data.entities.service.ServiceSearchSort.SortOrder.ASC;
import static com.zighter.zighterandroid.data.entities.service.ServiceSearchSort.SortOrder.DESC;
import static com.zighter.zighterandroid.data.entities.service.ServiceSearchSort.SortType.DATE;
import static com.zighter.zighterandroid.data.entities.service.ServiceSearchSort.SortType.PRICE;
import static com.zighter.zighterandroid.presentation.search.SearchExcursionsAdapter.OnSearchExcursionClickListener;

public class SearchFragment extends BaseSupportFragment
        implements SearchView, OnSearchExcursionClickListener {
    private static final String TAG = "SearchFragment";

    @NonNull
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @InjectPresenter
    SearchPresenter presenter;

    @ProvidePresenter
    public SearchPresenter providePresenter() {
        return searchPresenterProvider.get();
    }

    @Inject
    Provider<SearchPresenter> searchPresenterProvider;

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
    @BindView(R.id.sort_type)
    TextView sortType;

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
        if (getContext() == null) {
            return;
        }

        tryAgain.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        recyclerView.setAdapter(new SearchExcursionsAdapter(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                                              LinearLayoutManager.VERTICAL,
                                                              false));

        RxTextView.textChanges(toolbarSearch)
                .map(editable -> editable.toString().trim())
                .doOnNext(string -> {
                    if (string.isEmpty()) {
                        presenter.onSearchTyped(string, new ServiceSearchSort(null, null));
                    }
                })
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(string -> {
                    if (!string.isEmpty()) {
                        if (getContext() == null) {
                            return;
                        }
                        ServiceSearchSort currentSearchType = getCurrentSearchSort(getContext());
                        presenter.onSearchTyped(string, currentSearchType);
                    }
                });

        RxView.clicks(iconClear)
                .subscribe(it -> toolbarSearch.setText(null));

        RxView.clicks(sortType)
                .subscribe(it -> {
                    PopupMenu popup = new PopupMenu(getContext(), sortType);
                    popup.getMenuInflater().inflate(R.menu.search_sort, popup.getMenu());
                    popup.setOnMenuItemClickListener(item -> {
                        sortType.setText(item.getTitle());
                        presenter.onSearchTyped(toolbarSearch.getText().toString(),
                                                getCurrentSearchSort(getContext()));
                        return true;
                    });
                    popup.show();
                });
    }

    @NonNull
    private ServiceSearchSort getCurrentSearchSort(@NonNull Context context) {
        String sortTypeText = sortType.getText().toString();

        ServiceSearchSort serviceSearchSort = null;
        if (sortTypeText.equals(context.getString(R.string.cheapest_sort))) {
            serviceSearchSort = new ServiceSearchSort(PRICE, ASC);
        } else if (sortTypeText.equals(context.getString(R.string.expensive_sort))) {
            serviceSearchSort = new ServiceSearchSort(PRICE, DESC);
        } else if (sortTypeText.equals(context.getString(R.string.newest_sort))) {
            serviceSearchSort = new ServiceSearchSort(DATE, DESC);
        } else if (sortTypeText.equals(context.getString(R.string.oldest_sort))) {
            serviceSearchSort = new ServiceSearchSort(DATE, ASC);
        }

        if (serviceSearchSort == null) {
            serviceSearchSort = new ServiceSearchSort(null, null);
        }

        return serviceSearchSort;
    }

    @Override
    public void onSearchExcursionClicked(@NonNull ServiceSearchExcursion excursion) {
        // TODO
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
    public void showNotAuthorizedException() {
        if (getContext() == null) {
            return;
        }

        LoginActivity.start(getContext());
    }

    @Override
    public void showEmptyExcursions() {
        if (getContext() == null) {
            return;
        }

        errorMessage.setText(getContext().getString(R.string.nothing_was_found));
        tryAgain.setOnClickListener(view -> presenter.onSearchTyped(toolbarSearch.getText().toString(),
                                                                    getCurrentSearchSort(getContext())));

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showExcursions(@NonNull List<ServiceSearchExcursion> excursions) {
        SearchExcursionsAdapter adapter = (SearchExcursionsAdapter) recyclerView.getAdapter();
        adapter.setExcursions(excursions);
        adapter.notifyDataSetChanged();

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
        tryAgain.setOnClickListener(view -> presenter.onSearchTyped(toolbarSearch.getText().toString(),
                                                                    getCurrentSearchSort(getContext())));
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
        tryAgain.setOnClickListener(view -> presenter.onSearchTyped(toolbarSearch.getText().toString(),
                                                                    getCurrentSearchSort(getContext())));
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
        tryAgain.setOnClickListener(view -> presenter.onSearchTyped(toolbarSearch.getText().toString(),
                                                                    getCurrentSearchSort(getContext())));
        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }
}
