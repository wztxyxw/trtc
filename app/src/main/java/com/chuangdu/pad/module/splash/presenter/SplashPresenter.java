package com.chuangdu.pad.module.splash.presenter;


import com.chuangdu.pad.common.BasePresenter;
import com.chuangdu.pad.common.retrofit.RxUtil;
import com.chuangdu.pad.data.user.UserRepository;
import com.chuangdu.pad.module.splash.contract.SplashContract;

import javax.inject.Inject;

/**
 * @author sc
 * @since 2020-04-20
 */
public class SplashPresenter extends BasePresenter implements SplashContract.Presenter {
    private UserRepository mUserRepository;
    private SplashContract.View mView;
    @Inject
    public SplashPresenter(UserRepository userRepository, SplashContract.View view){
        this.mUserRepository = userRepository;
        this.mView = view;
    }
    @Override
    public void appVersion() {
        mUserRepository.appVersion().compose(RxUtil.applySchedulers())
                .doOnSubscribe(subscription -> {})
                .doAfterTerminate(() -> {})
                .subscribe(requestBean -> {
                    mView.versionSuccess(requestBean.getData());
                }, e -> {
                    mView.versionFail(errorMessage(e));
                });
    }
}
