package com.chuangdu.pad.module.login.contract;

import dagger.Module;
import dagger.Provides;

/**
 * 功能描述：
 * @author sc
 */
@Module
public class LoginModule {

    private LoginContract.View mView;

    public LoginModule(LoginContract.View view) {
        mView = view;
    }

    @Provides
    LoginContract.View provideLoginContractView() {
        return mView;
    }

}
