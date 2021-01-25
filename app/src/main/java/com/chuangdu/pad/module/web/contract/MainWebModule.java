package com.chuangdu.pad.module.web.contract;

import dagger.Module;
import dagger.Provides;

/**
 * @author sc
 * @since 2020-03-26
 */
@Module
public class MainWebModule {
    private MainWebContract.View mView;
    public MainWebModule(MainWebContract.View view) {
        mView = view;
    }

    @Provides
    MainWebContract.View provideMainWebContractView() {
        return mView;
    }

}
