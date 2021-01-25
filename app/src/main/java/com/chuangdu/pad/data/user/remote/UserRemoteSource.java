package com.chuangdu.pad.data.user.remote;

import com.chuangdu.pad.api.UserApi;
import com.chuangdu.pad.data.user.UserSource;
import com.chuangdu.pad.models.UploadFileModel;
import com.chuangdu.pad.models.UserInfoModel;
import com.chuangdu.pad.models.UserModel;
import com.chuangdu.pad.models.VersionModel;
import com.chuangdu.pad.models.req.LoginRequest;
import com.vo.network.BaseResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;

/**
 * @author sc
 * @since 2020-03-25
 */
public class UserRemoteSource implements UserSource {
    private UserApi mUserApi;

    private Flowable<BaseResponse<UserInfoModel>> mShareGetUserInfoFlowable;
    private long mLastGetUserInfoStamp;

    @Inject
    UserRemoteSource(UserApi userApi) {
        mUserApi = userApi;
    }

    @Override
    public Flowable<BaseResponse<VersionModel>> appVersion() {
        return mUserApi.appVersion();
    }

    @Override
    public Flowable<BaseResponse<UserModel>> login(LoginRequest param) {
        return mUserApi.login(param.toMap());
    }

    @Override
    public Flowable<BaseResponse<UserInfoModel>> userInfo() {
        return mUserApi.userInfo();
    }

    @Override
    public Flowable<BaseResponse> logout() {
        return mUserApi.logout();
    }

    @Override
    public UserInfoModel getUserInfoModel() {
        return null;
    }

    @Override
    public void setUserInfoModel(UserInfoModel model) {

    }

    @Override
    public Flowable<BaseResponse<List<UploadFileModel>>> uploadFile(MultipartBody.Part file) {
        return mUserApi.uploadFile(file);
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public boolean isHaveLogin() {
        return false;
    }

}
