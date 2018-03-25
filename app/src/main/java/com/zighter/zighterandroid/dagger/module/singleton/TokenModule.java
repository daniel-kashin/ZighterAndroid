package com.zighter.zighterandroid.dagger.module.singleton;

import android.content.Context;
import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.repositories.token.TokenStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TokenModule {
    @Provides
    @Singleton
    public TokenStorage provideTokenStorage(Context context) {
        return new TokenStorage(context);
    }
}
