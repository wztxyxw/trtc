package com.chuangdu.pad.data.user;

import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chuangdu.library.util.SP;
import com.chuangdu.pad.common.PathUtil;
import com.chuangdu.pad.data.user.local.UserLocalSource;
import com.chuangdu.pad.data.user.remote.UserRemoteSource;
import com.chuangdu.pad.event.LoginEvent;
import com.chuangdu.pad.models.UploadFileModel;
import com.chuangdu.pad.models.UserInfoModel;
import com.chuangdu.pad.models.UserModel;
import com.chuangdu.pad.models.VersionModel;
import com.chuangdu.pad.models.req.LoginRequest;
import com.vo.network.BaseResponse;
import com.vo.network.RetrofitRequestTool;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;

/**
 * @author sc
 * @since 2020-03-25
 */
public class UserRepository implements UserSource {

    private final UserLocalSource mLocalSource;
    private final UserRemoteSource mRemoteSource;
    private final SP mSP;

    @Inject
    public UserRepository(UserLocalSource localSource,
                          UserRemoteSource remoteSource,
                          SP sp) {
        mLocalSource = localSource;
        mRemoteSource = remoteSource;
        mSP = sp;
    }

    @Override
    public Flowable<BaseResponse<VersionModel>> appVersion() {
        return mRemoteSource.appVersion();
    }
    @Override
    public Flowable<BaseResponse<UserModel>> login(LoginRequest param) {
        return mRemoteSource.login(param).doOnNext(responseBean -> {
            UserModel info = responseBean.getData();
            if (info != null && !TextUtils.isEmpty(info.authorization)) {
                // 保存token
                saveToken(info.authorization);
                saveUserId(String.valueOf(info.userId));
                saveAccountId(String.valueOf(info.accountId));
                EventBus.getDefault().post(new LoginEvent(true));
                // 保存用户信息到本地

            }
        });
    }

    @Override
    public Flowable<BaseResponse<UserInfoModel>> userInfo() {
        return mRemoteSource.userInfo().doOnNext(responseBean -> {
            UserInfoModel info = responseBean.getData();
            if (info != null && !TextUtils.isEmpty(info.authorization)) {
                mLocalSource.setUserInfoModel(info);
            }
        });
    }

    @Override
    public Flowable<BaseResponse> logout() {

        clearUserCache();

        return mRemoteSource.logout().doOnNext(response -> {
        });
    }

    public void clearUserCache() {
        // 清空token
        saveToken(null);
        saveUserId(null);
        saveAccountId(null);
        // 清空用户信息
        mLocalSource.clearUserInfo();

        EventBus.getDefault().post(new LoginEvent(false));
    }


    @Override
    public UserInfoModel getUserInfoModel() {
        return mLocalSource.getUserInfoModel();
    }

    @Override
    public void setUserInfoModel(UserInfoModel model) {
        mLocalSource.setUserInfoModel(model);
    }

    @Override
    public Flowable<BaseResponse<List<UploadFileModel>>> uploadFile(MultipartBody.Part file) {
        return mRemoteSource.uploadFile(file);
    }

    @Override
    public boolean isLogin() {
        return mLocalSource.isLogin();
    }

    @Override
    public boolean isHaveLogin() {
        boolean status = mLocalSource.isLogin();
        if (!status) {
            ARouter.getInstance().build(PathUtil.PATH_LOGIN).navigation();
        }
        return status;
    }

    private void saveUserId(String userId) {
        RetrofitRequestTool.setUserId(mSP, userId);
    }

    private void saveAccountId(String accountId) {
        RetrofitRequestTool.setAccountId(mSP, accountId);
    }

    private void saveToken(String token) {
        RetrofitRequestTool.saveToken(mSP, token);
    }


    public boolean isSelf(int userId){
        return mLocalSource.getUserInfoModel().userId == userId;
    }

}
