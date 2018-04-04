package com.zighter.zighterandroid.data.repositories.token;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;

import io.reactivex.Single;

public class TokenStorage {
    @VisibleForTesting
    static final String TOKEN_PREFERENCES = "TOKEN_PREFERENCES";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_LOGIN = "KEY_LOGIN";

    @NonNull
    private final SharedPreferences sharedPreferences;

    public TokenStorage(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(TOKEN_PREFERENCES, Context.MODE_PRIVATE);
    }

    @SuppressLint("ApplySharedPref")
    public void saveToken(@Nullable String token) {
        sharedPreferences.edit()
                .putString(KEY_TOKEN, token)
                .commit();
    }

    @SuppressLint("ApplySharedPref")
    public void saveLogin(@Nullable String login) {
        sharedPreferences.edit()
                .putString(KEY_LOGIN, login)
                .commit();
    }

    @NonNull
    public Single<String> getTokenSingle() {
        return Single.fromCallable(this::getTokenOrThrowException);
    }

    @NonNull
    public Single<String> getLoginSingle() {
        return Single.fromCallable(this::getLoginOrThrowException);
    }

    public boolean isTokenPresent() {
        return getToken() != null;
    }

    @Nullable
    private String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    @Nullable
    private String getLogin() {
        return sharedPreferences.getString(KEY_LOGIN, null);
    }

    @NonNull
    private String getLoginOrThrowException() throws ServerNotAuthorizedException {
        String login = getLogin();

        if (login == null) {
            throw new ServerNotAuthorizedException(null);
        }

        return login;
    }

    @NonNull
    private String getTokenOrThrowException() throws ServerNotAuthorizedException {
        String token = getToken();

        if (token == null) {
            throw new ServerNotAuthorizedException(null);
        }

        return token;
    }
}
