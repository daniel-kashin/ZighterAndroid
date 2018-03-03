package com.zighter.zighterandroid.presentation.bottom_navigation;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.presentation.account.AccountFragment;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
import com.zighter.zighterandroid.presentation.excursion.map.ExcursionMapFragment;
import com.zighter.zighterandroid.presentation.bought_excursions.BoughtExcursionsFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.zighter.zighterandroid.data.download_excursion.DownloadExcursionNotificationContract.ACTION_OPEN_ACTIVITY;

public class BottomNavigationActivity extends BaseSupportActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    private Disposable showTabDisposable;

    @Override
    protected void onInjectDependencies() {
        // do nothing
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bottom_navigation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setOnNavigationClickListener();

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            bottomNavigationView.setSelectedItemId(R.id.action_my_excursions);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent == null || intent.getAction() == null) {
            return;
        }

        if (intent.getAction().equals(ACTION_OPEN_ACTIVITY)) {
            if (bottomNavigationView.getSelectedItemId() != R.id.action_my_excursions) {
                bottomNavigationView.setSelectedItemId(R.id.action_my_excursions);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (showTabDisposable != null) {
            showTabDisposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showTabDisposable != null) {
            showTabDisposable.dispose();
        }
    }

    private void setOnNavigationClickListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_my_excursions:
                case R.id.action_account:
                case R.id.action_interesting:
                case R.id.action_search:
                    openFragment(item.getItemId());
                    return true;
                default:
                    throw new IllegalStateException("Unknown item id");
            }
        });
    }

    private void openFragment(@IdRes int navigationItemId) {
        Fragment topFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Fragment fragmentToOpen;

        switch (navigationItemId) {
            case R.id.action_my_excursions:
                if (!(topFragment instanceof ExcursionMapFragment)) {
                    fragmentToOpen = BoughtExcursionsFragment.newInstance();
                    break;
                } else {
                    return;
                }
            case R.id.action_account:
            case R.id.action_interesting:
            case R.id.action_search:
                if (!(topFragment instanceof AccountFragment)) {
                    fragmentToOpen = AccountFragment.newInstance();
                    break;
                } else {
                    return;
                }
            default:
                throw new IllegalStateException("Unknown navigation item id");
        }

        if (showTabDisposable != null) {
            showTabDisposable.dispose();
        }
        showTabDisposable = Completable.timer(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentToOpen)
                        .commit()
                );
    }


}
