package com.zighter.zighterandroid.presentation.guide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.zighter.zighterandroid.R;
import com.zighter.zighterandroid.dagger.Injector;
import com.zighter.zighterandroid.data.entities.presentation.BoughtExcursionWithStatus;
import com.zighter.zighterandroid.data.entities.presentation.Guide;
import com.zighter.zighterandroid.presentation.common.BaseSupportActivity;
import com.zighter.zighterandroid.presentation.login.LoginActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zighter.zighterandroid.presentation.guide.GuidePresenter.GuidePresenterBuilder;

public class GuideActivity extends BaseSupportActivity implements GuideView {
    private static final String EXTRA_UUID = "EXTRA_UUID";

    public static void start(@NonNull Context context,
                             @NonNull String uuid) {
        Intent intent = new Intent(context, GuideActivity.class);
        intent.putExtra(EXTRA_UUID, uuid);
        context.startActivity(intent);
    }

    @InjectPresenter
    GuidePresenter presenter;

    @ProvidePresenter
    public GuidePresenter providePresenter() {
        if (uuid == null) {
            throw new IllegalStateException();
        }

        return guidePresenterBuilder.excursionUuid(uuid).build();
    }

    @Inject
    GuidePresenterBuilder guidePresenterBuilder;

    @Override
    protected void onInjectDependencies() {
        Injector.getInstance()
                .getGuideComponent()
                .inject(this);
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.face)
    ImageView face;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.try_again)
    TextView tryAgain;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.error_message)
    TextView errorMessage;

    @Nullable
    private String uuid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() == null) {
            throw new IllegalStateException();
        }
        uuid = getIntent().getStringExtra(EXTRA_UUID);

        super.onCreate(savedInstanceState);

        initializeView();
    }

    private void initializeView() {
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle.setText(getString(R.string.your_guide));

        tryAgain.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
        hideContent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void showNotAuthorizedException() {
        LoginActivity.start(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showGuide(@NonNull Guide guide) {
        String lastName = guide.getLastName();
        if (lastName != null && !lastName.isEmpty()) {
            lastName = " " + lastName;
        } else {
            lastName = "";
        }
        name.setText(guide.getFirstName() + lastName);
        phone.setText(guide.getPhone());
        email.setText(guide.getEmail());
        //noinspection ConstantConditions

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);

        showContent();
    }

    @Override
    public void showLoading() {
        hideContent();

        progressBar.setVisibility(View.VISIBLE);
        tryAgain.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showUnhandledException() {
        hideContent();

        errorMessage.setText(getString(R.string.unhandled_error_message));
        tryAgain.setOnClickListener(view -> presenter.onLoadGuide());

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNetworkUnavailable() {
        hideContent();

        errorMessage.setText(getString(R.string.network_error_message));
        tryAgain.setOnClickListener(view -> presenter.onLoadGuide());

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void showServerException() {
        hideContent();

        errorMessage.setText(getString(R.string.server_error_message));
        tryAgain.setOnClickListener(view -> presenter.onLoadGuide());

        progressBar.setVisibility(View.INVISIBLE);
        tryAgain.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void showContent() {
        image.setVisibility(View.VISIBLE);
        face.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
    }

    private void hideContent() {
        image.setVisibility(View.INVISIBLE);
        face.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
    }
}
