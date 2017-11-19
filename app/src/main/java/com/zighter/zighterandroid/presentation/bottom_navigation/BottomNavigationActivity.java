package com.zighter.zighterandroid.presentation.bottom_navigation;

import android.support.annotation.IdRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.presentation.account.AccountFragment;
import com.zighter.zighterandroid.presentation.navigation.NavigationFragment;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BottomNavigationActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        ButterKnife.bind(this);
        setOnNavigationClickListener();

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            bottomNavigationView.setSelectedItemId(R.id.action_my_routes);
        }
    }

    private void setOnNavigationClickListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_my_routes:
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
            case R.id.action_my_routes:
                if (!(topFragment instanceof NavigationFragment)) {
                    fragmentToOpen = NavigationFragment.newInstance();
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

        Completable.timer(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragmentToOpen)
                        .commit()
                );
    }


}
