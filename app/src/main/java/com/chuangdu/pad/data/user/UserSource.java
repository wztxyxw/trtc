package com.chuangdu.pad.data.user;

import com.chuangdu.pad.models.UploadFileModel;
import com.chuangdu.pad.models.UserInfoModel;
import com.chuangdu.pad.models.UserModel;
import com.chuangdu.pad.models.VersionModel;
import com.chuangdu.pad.models.req.LoginRequest;
import com.vo.network.BaseResponse;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;

/**
 * @author sc
 * @since 2020-03-25
 */
public interface UserSource {

    /**
     * 获取更新app版本接口
     * @return Flowable
     */
    Flowable<BaseResponse<VersionModel>> appVersion();
    /**
     * 登录
     * @param param
     * @return Flowable
     */
    Flowable<BaseResponse<UserModel>> login(LoginRequest param);

    Flowable<BaseResponse<UserInfoModel>> userInfo();

    /**
     * 退出登录
     * @return Flowable
     */
    Flowable<BaseResponse> logout();


    /**
     * 是否已登录
     * @return true=已登录
     */
    boolean isLogin();

    /**
     * 是否已登录
     * @return true=已登录
     */
    boolean isHaveLogin();

    /**
     * 获取本地用户信息
     * @return 用户信息
     */
    UserInfoModel getUserInfoModel();

    /**
     * 保存用户信息到本地
     * @param model 用户信息
     */
    void setUserInfoModel(UserInfoModel model);

    /**
     * 上传图片
     */
    Flowable<BaseResponse<List<UploadFileModel>>> uploadFile(MultipartBody.Part file);
}
