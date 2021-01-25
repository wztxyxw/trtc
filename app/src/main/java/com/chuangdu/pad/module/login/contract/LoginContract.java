package com.chuangdu.pad.module.login.contract;

import com.chuangdu.pad.common.i.IBasePresenter;
import com.chuangdu.pad.common.i.IBaseView;
import com.chuangdu.pad.models.UserInfoModel;
import com.chuangdu.pad.models.UserModel;
import com.chuangdu.pad.models.req.LoginRequest;

/**
 * @author sc
 * @since 2020-10-21
 */
public interface LoginContract {
    interface Model {
    }

    interface View extends IBaseView<Presenter> {
        void loginSuccess(UserModel model);
        void loginFail(String msg);

        void userInfoSuccess(UserInfoModel model);
        void userInfoFail(String msg);
    }

    interface Presenter extends IBasePresenter {

        void login(LoginRequest param);

        void userInfo();

        void logout();
    }
}
