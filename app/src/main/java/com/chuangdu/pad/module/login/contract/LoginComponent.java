package com.chuangdu.pad.module.login.contract;

import com.chuangdu.library.annotation.FragmentScrop;
import com.chuangdu.pad.common.dagger.AppComponent;
import com.chuangdu.pad.module.login.LoginActivity;

import dagger.Component;

/**
 * 功能描述：
 */
@FragmentScrop
@Component(dependencies = AppComponent.class,
        modules = LoginModule.class)
public interface LoginComponent {

    /**
     * dagger注入
     * @param loginActivity LoginActivity
     */
    void inject(LoginActivity loginActivity);

}
