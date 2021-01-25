package com.chuangdu.pad.module.splash.contract;

import dagger.Module;
import dagger.Provides;

/**
 * @author sc
 * @since 2020-03-26
 */
@Module
public class SplashModule {
    private SplashContract.View mView;
    public SplashModule(SplashContract.View view) {
        mView = view;
    }

    @Provides
    SplashContract.View provideSplashContractView() {
        return mView;
    }

}
