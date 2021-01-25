package com.chuangdu.pad.module.web.contract;

import dagger.Module;
import dagger.Provides;

/**
 * @author sc
 * @since 2020-03-26
 */
@Module
public class BrowserModule {
    private BrowserContract.View mView;
    public BrowserModule(BrowserContract.View view) {
        mView = view;
    }

    @Provides
    BrowserContract.View provideBrowserContractView() {
        return mView;
    }

}
