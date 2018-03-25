package com.zighter.zighterandroid.data.repositories.token;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.exception.ServerNotAuthorizedException;

import io.reactivex.Single;

public class TokenStorage {
    private static final String TOKEN_PREFERENCES = "TOKEN_PREFERENCES";
    private static final String KEY_TOKEN = "KEY_TOKEN";

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

    @Nullable
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    @NonNull
    public String getTokenOrThrowException() throws ServerNotAuthorizedException {
        String token = getToken();

        if (token == null) {
            throw new ServerNotAuthorizedException(null);
        }

        return token;
    }

    @NonNull
    public Single<String> getTokenSingle() {
        return Single.fromCallable(this::getTokenOrThrowException);
    }

    public boolean isTokenPresent() {
        return getToken() != null;
    }
}
