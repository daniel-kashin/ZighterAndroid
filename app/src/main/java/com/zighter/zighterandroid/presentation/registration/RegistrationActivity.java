package com.zighter.zighterandroid.presentation.registration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.zighter.zighterandroid.presentation.login.LoginPresenter;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.BindView;

public class RegistrationActivity extends BaseSupportActivity implements RegistrationView {
    public static void start(@NonNull Context context) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        context.startActivity(intent);
    }

    @InjectPresenter
    RegistrationPresenter presenter;

    @ProvidePresenter
    public RegistrationPresenter providePresenter() {
        return presenterProvider.get();
    }

    @Inject
    Provider<RegistrationPresenter> presenterProvider;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getRegistrationComponent()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_registration;
    }

    @BindView(R.id.username_layout)
    TextInputLayout usernameLayout;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;
    @BindView(R.id.email_layout)
    TextInputLayout emailLayout;
    @BindView(R.id.first_name_layout)
    TextInputLayout firstNameLayout;
    @BindView(R.id.second_name_layout)
    TextInputLayout secondNameLayout;
    @BindView(R.id.username)
    TextInputEditText username;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.email)
    TextInputEditText email;
    @BindView(R.id.first_name)
    TextInputEditText firstName;
    @BindView(R.id.second_name)
    TextInputEditText secondName;
    @BindView(R.id.login_button)
    Button loginButton;

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

            String emailText = email.getText().toString();
            if (emailText.length() < 5) {
                success = false;
                emailLayout.setError(getString(R.string.email_error));
            }

            String firstNameText = firstName.getText().toString();
            if (firstNameText.length() < 1) {
                success = false;
                firstNameLayout.setError(getString(R.string.first_name_error));
            }

            String secondNameText = secondName.getText().toString();
            if (secondNameText.length() < 1) {
                success = false;
                secondNameLayout.setError(getString(R.string.first_name_error));
            }

            if (success) {
                presenter.onRegister(emailText, firstNameText, secondNameText, passwordText, usernameText);
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
