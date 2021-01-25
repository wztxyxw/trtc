package com.chuangdu.pad.data.user.local;

import android.text.TextUtils;

import com.chuangdu.library.util.SP;
import com.chuangdu.pad.data.user.UserSource;
import com.chuangdu.pad.models.UploadFileModel;
import com.chuangdu.pad.models.UserInfoModel;
import com.chuangdu.pad.models.UserModel;
import com.chuangdu.pad.models.VersionModel;
import com.chuangdu.pad.models.req.LoginRequest;
import com.vo.network.BaseResponse;
import com.vo.network.RetrofitRequestTool;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;

/**
 * @author sc
 * @since 2020-03-25
 */
public class UserLocalSource implements UserSource {

    private SP mSP;
    private static final String LOGIN_INFO = "loginInfo";
    private static final String USER_DETAILINFO = "userDetailInfo";

    @Inject
    public UserLocalSource(SP sp) {
        mSP = sp;
    }

    @Override
    public Flowable<BaseResponse<VersionModel>> appVersion() {
        return null;
    }

    @Override
    public Flowable<BaseResponse<UserModel>> login(LoginRequest param) {
        return null;
    }

    @Override
    public Flowable<BaseResponse<UserInfoModel>> userInfo() {
        return null;
    }

    @Override
    public Flowable<BaseResponse> logout() {
        return null;
    }

    @Override
    public UserInfoModel getUserInfoModel() {
        return mSP.getObject(USER_DETAILINFO, UserInfoModel.class);
    }

    @Override
    public void setUserInfoModel(UserInfoModel model) {
        mSP.putObject(USER_DETAILINFO, model);
    }

    @Override
    public Flowable<BaseResponse<List<UploadFileModel>>> uploadFile(MultipartBody.Part file) {
        return null;
    }

    public void clearUserInfo() {
        setUserInfoModel(null);
    }

    @Override
    public boolean isLogin() {
        return !TextUtils.isEmpty(RetrofitRequestTool.getToken(mSP));
    }

    @Override
    public boolean isHaveLogin() {
        return false;
    }

}
