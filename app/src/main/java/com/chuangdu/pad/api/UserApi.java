package com.chuangdu.pad.api;

import com.chuangdu.pad.models.UploadFileModel;
import com.chuangdu.pad.models.UserInfoModel;
import com.chuangdu.pad.models.UserModel;
import com.chuangdu.pad.models.VersionModel;
import com.vo.network.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * @author sc
 */
public interface UserApi {

    /**
     * 获取更新app版本接口
     * @return Flowable
     */
    @POST("api/common/getAppVersion")
    Flowable<BaseResponse<VersionModel>> appVersion();
    /**
     * 登录
     * @param param
     * @return Flowable
     */
    @POST("api/common/android/login")
    Flowable<BaseResponse<UserModel>> login(@QueryMap Map<String, Object> param);

    /**
     * 获取本地用户信息
     * @return 用户信息
     */
    @POST("api/user/info")
    Flowable<BaseResponse<UserInfoModel>> userInfo();

    /**
     * 退出登录
     * @return Flowable
     */
    @POST("api/common/android/logout")
    Flowable<BaseResponse> logout();

    /**
     * 上传图片
     */
    @Multipart
    @POST("api/common/upload")
    Flowable<BaseResponse<List<UploadFileModel>>> uploadFile(@Part MultipartBody.Part file);

}
