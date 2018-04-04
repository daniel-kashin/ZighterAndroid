package com.zighter.zighterandroid.dagger.component;

import com.zighter.zighterandroid.dagger.module.RegistrationModule;
import com.zighter.zighterandroid.dagger.scope.RegistrationScope;
import com.zighter.zighterandroid.presentation.registration.RegistrationActivity;

import dagger.Subcomponent;

@RegistrationScope
@Subcomponent(modules = RegistrationModule.class)
public interface RegistrationComponent {
    void inject(RegistrationActivity registrationActivity);
}
