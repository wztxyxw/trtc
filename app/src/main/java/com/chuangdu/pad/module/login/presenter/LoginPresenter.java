package com.chuangdu.pad.module.login.presenter;

import com.chuangdu.pad.common.BasePresenter;
import com.chuangdu.pad.common.retrofit.RxUtil;
import com.chuangdu.pad.data.user.UserRepository;
import com.chuangdu.pad.models.req.LoginRequest;
import com.chuangdu.pad.module.login.contract.LoginContract;

import javax.inject.Inject;

/**
 * @author sc
 * @since 2020-10-21
 */
public class LoginPresenter extends BasePresenter implements LoginContract.Presenter {

    private final LoginContract.View mView;
    private final UserRepository mUserRepository;

    @Inject
    public LoginPresenter(LoginContract.View mView, UserRepository mUserRepository) {
        this.mView = mView;
        this.mUserRepository = mUserRepository;
    }

    @Override
    public void login(LoginRequest param) {
        mUserRepository.login(param).compose(RxUtil.applySchedulers())
                .doOnSubscribe(subscription -> mView.showLoadingView(true))
                .doAfterTerminate(() -> mView.hideLoadingView())
                .subscribe(requestBean -> {
                    mView.loginSuccess(requestBean.getData());
                }, e -> mView.loginFail(errorMessage(e)));
    }

    @Override
    public void userInfo() {
        mUserRepository.userInfo().compose(RxUtil.applySchedulers())
                .doOnSubscribe(subscription -> mView.showLoadingView(true))
                .doAfterTerminate(() -> mView.hideLoadingView())
                .subscribe(requestBean -> {
                    mView.userInfoSuccess(requestBean.getData());
                }, e -> mView.userInfoFail(errorMessage(e)));

    }

    @Override
    public void logout() {
        mUserRepository.logout().compose(RxUtil.applySchedulers()).subscribe(responseBean -> {
        }, ex -> {

        });
    }
}
