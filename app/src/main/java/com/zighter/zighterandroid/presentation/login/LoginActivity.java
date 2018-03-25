package com.zighter.zighterandroid.presentation.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.presentation.bottom_navigation.BottomNavigationActivity;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

public class LoginActivity extends BaseSupportActivity implements LoginView {
    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @InjectPresenter
    LoginPresenter presenter;

    @ProvidePresenter
    public LoginPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Inject
    Provider<LoginPresenter> presenterProvider;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getLoginComponent()
                .inject(this);
    }

    @BindView(R.id.username_layout)
    TextInputLayout usernameLayout;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.username)
    TextInputEditText username;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.login_button)
    Button loginButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView();
    }

    private void initializeView() {
        loginButton.setOnClickListener(view -> {
            boolean success = true;

            String usernameText = username.getText().toString();
            if (usernameText.length() < 1) {
                success = false;
                usernameLayout.setError(getString(R.string.username_error));
            }

            String passwordText = password.getText().toString();
            if (passwordText.length() < 1) {
                success = false;
                passwordLayout.setError(getString(R.string.password_error));
            }

            if (success) {
                presenter.onLogin(usernameText, passwordText);
            }
        });
    }

    @Override
    public void showNetworkUnavailable() {
        Toast.makeText(this, getString(R.string.network_error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerException() {
        Toast.makeText(this, getString(R.string.server_error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUnhandledException() {
        Toast.makeText(this, getString(R.string.unhandled_error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showServerLoginException() {
        Toast.makeText(this, getString(R.string.server_login_error_message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openBottomNavigation() {
        BottomNavigationActivity.start(this);
    }
}
