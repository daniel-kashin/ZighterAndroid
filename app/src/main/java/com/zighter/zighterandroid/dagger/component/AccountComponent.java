package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.AccountModule;
import com.zighter.zighterandroid.dagger.scope.AccountScope;
import com.zighter.zighterandroid.presentation.account.AccountFragment;

import dagger.Subcomponent;

@Subcomponent(modules = AccountModule.class)
@AccountScope
public interface AccountComponent {
    void inject(AccountFragment accountFragment);
}
