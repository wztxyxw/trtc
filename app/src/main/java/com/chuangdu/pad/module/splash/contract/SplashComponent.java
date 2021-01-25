package com.chuangdu.pad.module.splash.contract;


import com.chuangdu.library.annotation.FragmentScrop;
import com.chuangdu.pad.common.dagger.AppComponent;
import com.chuangdu.pad.module.splash.SplashActivity;

import dagger.Component;

/**
 * @author sc
 * @since 2020-03-26
 */
@FragmentScrop
@Component(dependencies = AppComponent.class,
        modules = SplashModule.class)
public interface SplashComponent {
    void inject(SplashActivity activity);
}
