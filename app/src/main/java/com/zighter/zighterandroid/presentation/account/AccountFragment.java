package com.zighter.zighterandroid.presentation.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.presentation.bought_excursions.BoughtExcursionsPresenter;
import com.zighter.zighterandroid.presentation.common.BaseSupportFragment;
import com.zighter.zighterandroid.presentation.login.LoginActivity;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

public class AccountFragment extends BaseSupportFragment implements AccountView {
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @InjectPresenter
    AccountPresenter presenter;

    @ProvidePresenter
    public AccountPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Inject
    Provider<AccountPresenter> presenterProvider;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getAccountComponent()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_account;
    }

    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.log_out_button)
    Button logOutButton;
    @BindView(R.id.login)
    TextView loginView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeView();
    }

    private void initializeView() {
        rootView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showInfo(@NonNull String login) {
        loginView.setText(login);
        logOutButton.setOnClickListener(view -> {
            presenter.onLogOutClicked();
        });

        rootView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNotAuthorizedException() {
        if (getContext() == null) {
            return;
        }

        LoginActivity.start(getContext());
    }
}
